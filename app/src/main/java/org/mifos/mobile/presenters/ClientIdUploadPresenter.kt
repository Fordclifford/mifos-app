package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

import okhttp3.ResponseBody

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.ClientIdUploadView
import org.mifos.mobile.utils.FileUtils
import org.mifos.mobile.utils.MFErrorParser
import java.io.File

import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
class ClientIdUploadPresenter @Inject constructor(
    private val dataManager: DataManager?,
    @ApplicationContext context: Context?
) : BasePresenter<ClientIdUploadView?>(context) {
    private val compositeDisposables: CompositeDisposable = CompositeDisposable()
    fun attachView(mvpView: ClientIdUploadView) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposables.clear()
    }

    fun uploadDocument(clientId: Long, idFrontFile: File?, idBackFile: File?,entity:String) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.createDocument(
            clientId, idFrontFile!!.name,
            "$entity National ID Front Side", getRequestFileBody(idFrontFile)
        )
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mvpView?.hideProgress()
                    mvpView?.showError(MFErrorParser.errorMessage(e))
                }
                override fun onNext(resp: ResponseBody) {
                    dataManager.createDocument(
                        clientId, idFrontFile.name,
                        "Member National ID Back Side", getRequestFileBody(
                            idBackFile!!
                        )
                    )
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribeOn(Schedulers.io())
                        ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                            override fun onComplete() {}
                            override fun onError(e: Throwable) {
                                mvpView?.hideProgress()
                                mvpView?.showError(MFErrorParser.errorMessage(e))
                            }
                            override fun onNext(resp: ResponseBody) {
                                mvpView?.hideProgress()
                                mvpView?.showUploadedSuccessfully()
                            }
                        })?.let { compositeDisposables.add(it) }
                }
            })?.let { compositeDisposables.add(it) }
    }

    private fun getRequestFileBody(file: File): MultipartBody.Part? {
        // create RequestBody instance from file
        val requestFile = RequestBody.create(MediaType.parse(FileUtils.getMimeType(file.path)), file)

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }


}
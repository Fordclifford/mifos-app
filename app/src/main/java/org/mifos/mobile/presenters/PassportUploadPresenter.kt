package org.mifos.mobile.presenters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
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
import org.mifos.mobile.ui.views.PassportUploadView
import org.mifos.mobile.utils.FileUtils.getMimeType
import org.mifos.mobile.utils.MFErrorParser
import java.io.File
import javax.inject.Inject


/**
 * Created by dilpreet on 31/7/17.
 */
class PassportUploadPresenter @Inject constructor(
    private val dataManager: DataManager?,
    @ApplicationContext context: Context?
) : BasePresenter<PassportUploadView?>(context) {
    private val compositeDisposables: CompositeDisposable = CompositeDisposable()
    fun attachView(mvpView: PassportUploadView) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposables.clear()
    }

    fun createImage(clientId: Long, file: File?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.createImage(clientId, getRequestFileBody(file!!))
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

    private fun getRequestFileBody(file: File): MultipartBody.Part {
        // create RequestBody instance from file

        val requestFile = RequestBody.create(MediaType.parse(getMimeType(file.path)), file)

//      return MultipartBody.create(MediaType.parse("multipart/form-data"),file)
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }



}
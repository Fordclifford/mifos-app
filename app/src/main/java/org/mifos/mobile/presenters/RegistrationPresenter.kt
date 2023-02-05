package org.mifos.mobile.presenters

import android.content.Context
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.client.ClientResp
import org.mifos.mobile.models.register.ClientUserRegisterPayload
import org.mifos.mobile.models.register.IdentifierPayload
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.RegistrationView
import org.mifos.mobile.utils.MFErrorParser
import javax.inject.Inject


/**
 * Created by dilpreet on 31/7/17.
 */
class RegistrationPresenter @Inject constructor(
    private val dataManager: DataManager?,
    @ApplicationContext context: Context?
) : BasePresenter<RegistrationView?>(context) {
    private val compositeDisposables: CompositeDisposable = CompositeDisposable()
    fun attachView(mvpView: RegistrationView) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposables.clear()
    }

    fun registerUser(
        kraPayload: IdentifierPayload?,
        idPayload: IdentifierPayload?,
        clientUserRegisterPayload: ClientUserRegisterPayload?
    ) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.registerClientUser(clientUserRegisterPayload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ClientResp?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mvpView?.hideProgress()
                    mvpView?.showError(MFErrorParser.errorMessage(e))
                }
                override fun onNext(resp: ClientResp) {
                    Log.d("Returned Resp", resp.toString())
                    val arrayIdentifierPayload = ArrayList<IdentifierPayload?>()
                    arrayIdentifierPayload.add(kraPayload)
                    arrayIdentifierPayload.add(idPayload)
                   createIdentifier(resp.clientId!!,arrayIdentifierPayload)
                }
            })?.let { compositeDisposables.add(it) }
    }

     fun createIdentifier(
        clientId: Long,
        arrayIdentifierPayload: ArrayList<IdentifierPayload?>
    ) {
        dataManager?.createIdentifier(clientId, arrayIdentifierPayload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onNext(responseBody: ResponseBody) {
                    mvpView?.hideProgress()
                    mvpView?.showRegisteredSuccessfully(clientId)
                }
            })?.let { compositeDisposables.add(it) }
    }

}
package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import okhttp3.ResponseBody

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.register.IdentifierPayload
import org.mifos.mobile.models.register.RegisterPayload
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
        registerPayload: RegisterPayload?
    ) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.registerUser(registerPayload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mvpView?.hideProgress()
                    mvpView?.showError(MFErrorParser.errorMessage(e))
                }

                override fun onNext(responseBody: ResponseBody) {
                    val clientId = 669L
                    dataManager.createIdentifier(clientId, idPayload)
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribeOn(Schedulers.io())
                        ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                            override fun onComplete() {}
                            override fun onError(e: Throwable) {
                            }

                            override fun onNext(responseBody: ResponseBody) {
                                createIdentity(clientId, kraPayload)
                            }
                        })?.let { compositeDisposables.add(it) }
                }
            })?.let { compositeDisposables.add(it) }
    }

    fun createIdentity(clientId: Long?, identifierPayload: IdentifierPayload?) {
        checkViewAttached()
        dataManager?.createIdentifier(clientId, identifierPayload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                }

                override fun onNext(responseBody: ResponseBody) {
                    mvpView?.hideProgress()
                    mvpView?.showRegisteredSuccessfully()
                }
            })?.let { compositeDisposables.add(it) }
    }

}
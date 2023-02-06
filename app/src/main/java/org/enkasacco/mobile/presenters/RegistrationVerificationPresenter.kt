package org.enkasacco.mobile.presenters

import android.content.Context
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.enkasacco.mobile.api.DataManager
import org.enkasacco.mobile.injection.ApplicationContext
import org.enkasacco.mobile.models.client.NextOfKinPayload
import org.enkasacco.mobile.models.passwordreset.NewpasswordPayload
import org.enkasacco.mobile.models.passwordreset.ResetPayload
import org.enkasacco.mobile.models.register.UserVerify
import org.enkasacco.mobile.models.templates.client.FamilyMemberOptions
import org.enkasacco.mobile.models.templates.client.Options
import org.enkasacco.mobile.presenters.base.BasePresenter
import org.enkasacco.mobile.ui.views.RegistrationVerificationView
import org.enkasacco.mobile.utils.MFErrorParser.errorMessage
import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
class RegistrationVerificationPresenter @Inject constructor(
    private val dataManager: DataManager?,
    @ApplicationContext context: Context?
) : BasePresenter<RegistrationVerificationView?>(context) {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        compositeDisposables.clear()
    }

    fun verifyUser(userVerify: UserVerify?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.verifyUser(userVerify)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mvpView?.hideProgress()
                    mvpView?.showError(errorMessage(e))
                }

                override fun onNext(responseBody: ResponseBody) {
                    mvpView?.hideProgress()
                    mvpView?.showVerifiedSuccessfully()
                }
            })?.let { compositeDisposables.add(it) }
    }

    fun locaFamilyTemplate(clientId: Long?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.loadFamilyTemplate(clientId!!)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<FamilyMemberOptions?>() {
                override fun onError(e: Throwable) {
                    mvpView!!.hideProgress()
                    mvpView?.showError(errorMessage(e))
                }

                override fun onComplete() {
                }

                override fun onNext(t: FamilyMemberOptions) {
                    Log.d("Options Fetched",t.toString());
                    mvpView!!.hideProgress()
                    mvpView!!.showClientTemplate(t)
                }
            })?.let { compositeDisposables.add(it) }
    }

    fun createNok(payload: NextOfKinPayload, clientId: Long?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.createNok(payload,clientId)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mvpView?.hideProgress()
                    mvpView?.showError(errorMessage(e))
                }

                override fun onNext(responseBody: ResponseBody) {
                    mvpView?.hideProgress()
                    mvpView?.showVerifiedSuccessfully()
                }
            })?.let { compositeDisposables.add(it) }
    }

    fun requestToken(payload: ResetPayload) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.requestToken(payload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mvpView?.hideProgress()
                    mvpView?.showError(errorMessage(e))
                }

                override fun onNext(responseBody: ResponseBody) {
                    mvpView?.hideProgress()
                    mvpView?.showVerifiedSuccessfully()
                }
            })?.let { compositeDisposables.add(it) }
    }

    fun resetPassword(payload: NewpasswordPayload) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.resetPass(payload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mvpView?.hideProgress()
                    mvpView?.showError(errorMessage(e))
                }

                override fun onNext(responseBody: ResponseBody) {
                    mvpView?.hideProgress()
                    mvpView?.showVerifiedSuccessfully()
                }
            })?.let { compositeDisposables.add(it) }
    }

}
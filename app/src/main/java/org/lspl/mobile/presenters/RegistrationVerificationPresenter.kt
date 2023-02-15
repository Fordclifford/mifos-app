package org.lspl.mobile.presenters

import android.content.Context
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.lspl.mobile.api.DataManager
import org.lspl.mobile.injection.ApplicationContext
import org.lspl.mobile.models.client.NextOfKinPayload
import org.lspl.mobile.models.passwordreset.NewpasswordPayload
import org.lspl.mobile.models.passwordreset.ResetPayload
import org.lspl.mobile.models.register.UserVerify
import org.lspl.mobile.models.templates.client.FamilyMemberOptions
import org.lspl.mobile.presenters.base.BasePresenter
import org.lspl.mobile.ui.views.RegistrationVerificationView
import org.lspl.mobile.utils.MFErrorParser.errorMessage
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
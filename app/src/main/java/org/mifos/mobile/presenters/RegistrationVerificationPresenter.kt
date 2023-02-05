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
import org.mifos.mobile.models.client.NextOfKinPayload
import org.mifos.mobile.models.register.UserVerify
import org.mifos.mobile.models.templates.client.FamilyMemberOptions
import org.mifos.mobile.models.templates.client.Options
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.RegistrationVerificationView
import org.mifos.mobile.utils.MFErrorParser.errorMessage
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

    fun filterOptions(options: List<Options>?): List<String> {
        val filterValues: MutableList<String> = ArrayList()
        rx.Observable.from(options)
            .subscribe { options -> filterValues.add(options.name!!) }
        return filterValues
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

}
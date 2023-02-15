package org.lspl.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import okhttp3.ResponseBody

import org.lspl.mobile.api.DataManager
import org.lspl.mobile.injection.ApplicationContext
import org.lspl.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.lspl.mobile.presenters.base.BasePresenter
import org.lspl.mobile.ui.views.SavingsAccountWithdrawView

import javax.inject.Inject

/*
* Created by saksham on 02/July/2018
*/
class SavingsAccountWithdrawPresenter @Inject constructor(
        private val dataManager: DataManager?,
        @ApplicationContext context: Context?
) : BasePresenter<SavingsAccountWithdrawView?>(context) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    fun submitWithdrawSavingsAccount(
            accountId: String?,
            payload: SavingsAccountWithdrawPayload?
    ) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.submitWithdrawSavingsAccount(accountId, payload)
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onNext(responseBodyObservable: ResponseBody) {
                        mvpView?.hideProgress()
                        mvpView?.showSavingsAccountWithdrawSuccessfully()
                    }

                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(e.message ?: "")
                    }

                    override fun onComplete() {}
                })?.let { compositeDisposable.add(it) }
    }

}
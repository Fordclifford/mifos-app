package org.enkasacco.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.enkasacco.mobile.R
import org.enkasacco.mobile.api.DataManager
import org.enkasacco.mobile.injection.ActivityContext
import org.enkasacco.mobile.models.Charge
import org.enkasacco.mobile.models.Page
import org.enkasacco.mobile.presenters.base.BasePresenter
import org.enkasacco.mobile.ui.views.ClientChargeView

import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
class ClientChargePresenter @Inject constructor(private val dataManager: DataManager?, @ActivityContext context: Context?) :
        BasePresenter<ClientChargeView?>(context) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    fun loadClientCharges(clientId: Long) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.getClientCharges(clientId)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<Page<Charge?>?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showErrorFetchingClientCharges(
                                context?.getString(R.string.client_charges))
                    }

                    override fun onNext(chargePage: Page<Charge?>) {
                        mvpView?.hideProgress()
                        mvpView?.showClientCharges(chargePage.pageItems)
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

    fun loadLoanAccountCharges(loanId: Long) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.getLoanCharges(loanId)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<List<Charge?>?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showErrorFetchingClientCharges(
                                context?.getString(R.string.client_charges))
                    }

                    override fun onNext(chargeList: List<Charge?>) {
                        mvpView?.hideProgress()
                        mvpView?.showClientCharges(chargeList)
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

    fun loadSavingsAccountCharges(savingsId: Long) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.getSavingsCharges(savingsId)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<List<Charge?>?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showErrorFetchingClientCharges(
                                context?.getString(R.string.client_charges))
                    }

                    override fun onNext(chargeList: List<Charge?>) {
                        mvpView?.hideProgress()
                        mvpView?.showClientCharges(chargeList)
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

    fun loadClientLocalCharges() {
        checkViewAttached()
        dataManager?.clientLocalCharges
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<Page<Charge?>?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.showErrorFetchingClientCharges(
                                context?.getString(R.string.client_charges))
                    }

                    override fun onNext(chargePage: Page<Charge?>) {
                        mvpView?.showClientCharges(chargePage.pageItems)
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

}
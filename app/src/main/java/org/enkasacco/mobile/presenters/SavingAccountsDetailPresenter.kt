package org.enkasacco.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.enkasacco.mobile.R
import org.enkasacco.mobile.api.DataManager
import org.enkasacco.mobile.injection.ApplicationContext
import org.enkasacco.mobile.models.accounts.savings.SavingsWithAssociations
import org.enkasacco.mobile.presenters.base.BasePresenter
import org.enkasacco.mobile.ui.views.SavingAccountsDetailView
import org.enkasacco.mobile.utils.Constants

import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 18/8/16.
 */
class SavingAccountsDetailPresenter @Inject constructor(
        private val dataManager: DataManager?,
        @ApplicationContext context: Context?
) : BasePresenter<SavingAccountsDetailView?>(context) {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        compositeDisposables.clear()
    }

    /**
     * Load details of a particular saving account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     *
     * @param accountId Id of Savings Account
     */
    fun loadSavingsWithAssociations(accountId: Long?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.getSavingsWithAssociations(accountId,
                Constants.TRANSACTIONS)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<SavingsWithAssociations?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showErrorFetchingSavingAccountsDetail(
                                context?.getString(R.string.error_saving_account_details_loading))
                    }

                    override fun onNext(savingAccount: SavingsWithAssociations) {
                        mvpView?.hideProgress()
                        mvpView?.showSavingAccountsDetail(savingAccount)
                    }
                })?.let {
                    compositeDisposables.add(it
                    )
                }
    }

}
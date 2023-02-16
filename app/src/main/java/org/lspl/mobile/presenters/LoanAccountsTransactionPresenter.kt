package org.lspl.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.lspl.mobile.R
import org.lspl.mobile.api.DataManager
import org.lspl.mobile.injection.ApplicationContext
import org.lspl.mobile.models.accounts.loan.LoanWithAssociations
import org.lspl.mobile.presenters.base.BasePresenter
import org.lspl.mobile.ui.views.LoanAccountsTransactionView
import org.lspl.mobile.utils.Constants

import javax.inject.Inject

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/ /**
 * Created by dilpreet on 4/3/17.
 */
class LoanAccountsTransactionPresenter @Inject constructor(
        private val dataManager: DataManager?,
        @ApplicationContext context: Context?
) : BasePresenter<LoanAccountsTransactionView?>(context) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    /**
     * Load details of a particular loan account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     *
     * @param loanId Id of Loan Account
     */
    fun loadLoanAccountDetails(loanId: Long?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.getLoanWithAssociations(Constants.TRANSACTIONS, loanId)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<LoanWithAssociations?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showErrorFetchingLoanAccountsDetail(
                                context?.getString(R.string.loan_transaction_details))
                    }

                    override fun onNext(loanWithAssociations: LoanWithAssociations) {
                        mvpView?.hideProgress()
                        if (loanWithAssociations.transactions != null &&
                                loanWithAssociations.transactions?.isNotEmpty() == true) {
                            mvpView?.showLoanTransactions(loanWithAssociations)
                        } else {
                            mvpView?.showEmptyTransactions(loanWithAssociations)
                        }
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

}
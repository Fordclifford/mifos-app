package org.enkasacco.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.enkasacco.mobile.R
import org.enkasacco.mobile.api.DataManager
import org.enkasacco.mobile.injection.ApplicationContext
import org.enkasacco.mobile.models.Page
import org.enkasacco.mobile.models.Transaction
import org.enkasacco.mobile.presenters.base.BasePresenter
import org.enkasacco.mobile.ui.views.RecentTransactionsView

import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 10/08/16
 */
class RecentTransactionsPresenter @Inject constructor(
        private val dataManager: DataManager?,
        @ApplicationContext context: Context?
) : BasePresenter<RecentTransactionsView?>(context) {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()
    private val limit = 50
    private var loadmore = false

    override fun detachView() {
        super.detachView()
        compositeDisposables.clear()
    }

    /**
     * Used to call function `loadRecentTransactions(int offset, int limit)` which is used for
     * fetching RecentTransaction from server.
     *
     * @param loadmore Set `false` if calling First time and `true` if you need to fetch
     * more [Transaction]
     * @param offset   Set `0` if calling first time or set length of `totalItemsCount`
     * if
     * you need to fetch more [Transaction]
     */
    fun loadRecentTransactions(loadmore: Boolean, offset: Int) {
        this.loadmore = loadmore
        loadRecentTransactions(offset, limit)
    }

    /**
     * Used to load List of [Transaction] from server depending upon the `offset` and
     * the max `limit` and notifies the view to display it. And in case of any
     * error during fetching the required details it notifies the view.
     *
     * @param offset Starting position for fetching the list of [Transaction]
     * @param limit  Maximum size of List of [Transaction] which is fetched from server
     */
    private fun loadRecentTransactions(offset: Int, limit: Int) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.getRecentTransactions(offset, limit)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<Page<Transaction?>?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showErrorFetchingRecentTransactions(
                                context?.getString(R.string.recent_transactions))
                    }

                    override fun onNext(transactions: Page<Transaction?>) {
                        mvpView?.hideProgress()
                        if (transactions.totalFilteredRecords == 0) {
                            mvpView?.showEmptyTransaction()
                        } else if (loadmore && transactions.pageItems.isNotEmpty()) {
                            mvpView
                                    ?.showLoadMoreRecentTransactions(transactions.pageItems)
                        } else if (transactions.pageItems.isNotEmpty()) {
                            mvpView?.showRecentTransactions(transactions.pageItems)
                        }
                    }
                })?.let {
                    compositeDisposables.add(it
                    )
                }
    }

}
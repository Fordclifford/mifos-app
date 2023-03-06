package org.lspl.mobile

import android.content.Context

import io.reactivex.Observable

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.lspl.mobile.api.DataManager
import org.lspl.mobile.models.accounts.loan.LoanWithAssociations
import org.lspl.mobile.presenters.LoanAccountsDetailPresenter
import org.lspl.mobile.ui.views.LoanAccountsDetailView
import org.lspl.mobile.util.RxSchedulersOverrideRule
import org.lspl.mobile.utils.Constants
<<<<<<< HEAD
=======
import org.mifos.mobile.FakeRemoteDataSource
>>>>>>> 1fea8c461ffd9e5ad3acc64efd9af8145187cd91

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 27/6/17.
 */
@RunWith(MockitoJUnitRunner::class)
class LoanAccountDetailPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: LoanAccountsDetailView? = null
    private var presenter: LoanAccountsDetailPresenter? = null
    private var loanWithAssociations: LoanWithAssociations? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = LoanAccountsDetailPresenter(dataManager!!, context)
        presenter?.attachView(view)
        loanWithAssociations = FakeRemoteDataSource.loanAccountWithTransaction
    }

    @Test
    @Throws(Exception::class)
    fun testLoadLoanAccountDetails() {
        Mockito.`when`(dataManager?.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, 4)).thenReturn(Observable.just(loanWithAssociations))
        presenter?.loadLoanAccountDetails(4)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showLoanAccountsDetail(loanWithAssociations)
        Mockito.verify(view, Mockito.never())?.showErrorFetchingLoanAccountsDetail(context?.getString(R.string.error_loan_account_details_loading))
    }

    @Test
    @Throws(Exception::class)
    fun testLoadLoanAccountDetailsFails() {
        Mockito.`when`(dataManager?.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, 4)).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadLoanAccountDetails(4)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showErrorFetchingLoanAccountsDetail(context?.getString(R.string.error_loan_account_details_loading))
        Mockito.verify(view, Mockito.never())?.showLoanAccountsDetail(null)
    }
}
package org.lspl.mobile

import android.content.Context

import io.reactivex.Observable

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.lspl.mobile.api.DataManager
import org.lspl.mobile.models.accounts.loan.LoanWithAssociations
import org.lspl.mobile.presenters.LoanRepaymentSchedulePresenter
import org.lspl.mobile.ui.views.LoanRepaymentScheduleMvpView
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
class LoanRepaymentSchedulePresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: LoanRepaymentScheduleMvpView? = null
    private var presenter: LoanRepaymentSchedulePresenter? = null
    private var loanWithRepaymentSchedule: LoanWithAssociations? = null
    private var loanWithEmptyRepaymentSchedule: LoanWithAssociations? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = LoanRepaymentSchedulePresenter(context, dataManager!!)
        presenter?.attachView(view)
        loanWithRepaymentSchedule = FakeRemoteDataSource.loanAccountRepaymentSchedule
        loanWithEmptyRepaymentSchedule = FakeRemoteDataSource.loanAccountEmptyRepaymentSchedule
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    @Throws(Exception::class)
    fun testLoadLoanAccountDetails() {
        Mockito.`when`(dataManager?.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, 29)).thenReturn(Observable.just(loanWithRepaymentSchedule))
        presenter?.loanLoanWithAssociations(29)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showLoanRepaymentSchedule(loanWithRepaymentSchedule)
        Mockito.verify(view, Mockito.never())?.showEmptyRepaymentsSchedule(null)
        Mockito.verify(view, Mockito.never())?.showError(context?.getString(R.string.error_fetching_repayment_schedule))
    }

    @Test
    @Throws(Exception::class)
    fun testLoadLoanAccountDetailsEmpty() {
        Mockito.`when`(dataManager?.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, 29)).thenReturn(Observable.just(loanWithEmptyRepaymentSchedule))
        presenter?.loanLoanWithAssociations(29)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showEmptyRepaymentsSchedule(loanWithEmptyRepaymentSchedule)
        Mockito.verify(view, Mockito.never())?.showLoanRepaymentSchedule(null)
        Mockito.verify(view, Mockito.never())?.showError(context?.getString(R.string.error_fetching_repayment_schedule))
    }

    @Test
    @Throws(Exception::class)
    fun testLoadLoanAccountDetailsFails() {
        Mockito.`when`(dataManager?.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, 29)).thenReturn(Observable.error(RuntimeException()))
        presenter?.loanLoanWithAssociations(29)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(context?.getString(R.string.error_fetching_repayment_schedule))
        Mockito.verify(view, Mockito.never())?.showEmptyRepaymentsSchedule(null)
        Mockito.verify(view, Mockito.never())?.showLoanRepaymentSchedule(null)
    }
}
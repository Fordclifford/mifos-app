package org.lspl.mobile

import android.content.Context

import io.reactivex.Observable

import okhttp3.ResponseBody

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.lspl.mobile.api.DataManager
import org.lspl.mobile.models.payload.LoansPayload
import org.lspl.mobile.models.templates.loans.LoanTemplate
import org.lspl.mobile.presenters.LoanApplicationPresenter
import org.lspl.mobile.ui.enums.LoanState
import org.lspl.mobile.ui.views.LoanApplicationMvpView
import org.lspl.mobile.util.RxSchedulersOverrideRule

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 12/7/17.
 */
@RunWith(MockitoJUnitRunner::class)
class LoanApplicationPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: LoanApplicationMvpView? = null

    @Mock
    var mockedResponseBody: ResponseBody? = null
    private var presenter: LoanApplicationPresenter? = null
    private var loanTemplate: LoanTemplate? = null
    private var loanTemplateWithProduct: LoanTemplate? = null
    private var loansPayload: LoansPayload? = null

    @Before
    fun setUp() {
        presenter = LoanApplicationPresenter(dataManager!!, context)
        presenter?.attachView(view)
        loanTemplate = FakeRemoteDataSource.loanTemplate
        loanTemplateWithProduct = FakeRemoteDataSource.loanTemplateByTemplate
        loansPayload = FakeRemoteDataSource.loansPayload
    }

    @After
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testLoadLoanApplicationTemplateNew() {
        val loanState = LoanState.CREATE
        Mockito.`when`(dataManager?.loanTemplate).thenReturn(Observable.just(loanTemplate))
        presenter?.loadLoanApplicationTemplate(loanState)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showLoanTemplate(loanTemplate)
        Mockito.verify(view, Mockito.never())?.showError(context?.getString(R.string.error_fetching_template))
    }

    @Test
    fun testLoadLoanApplicationTemplateUpdate() {
        val loanState = LoanState.UPDATE
        Mockito.`when`(dataManager?.loanTemplate).thenReturn(Observable.just(loanTemplate))
        presenter?.loadLoanApplicationTemplate(loanState)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showUpdateLoanTemplate(loanTemplate)
        Mockito.verify(view, Mockito.never())?.showError(context?.getString(R.string.error_fetching_template))
    }

    @Test
    fun testLoadLoanApplicationTemplateFails() {
        val loanState = LoanState.CREATE
        Mockito.`when`(dataManager?.loanTemplate).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadLoanApplicationTemplate(loanState)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(context?.getString(R.string.error_fetching_template))
        Mockito.verify(view, Mockito.never())?.showLoanTemplate(loanTemplate)
    }

    @Test
    fun testLoadLoanApplicationTemplateByProductNew() {
        val loanState = LoanState.CREATE
        Mockito.`when`(dataManager?.getLoanTemplateByProduct(1)).thenReturn(Observable.just(loanTemplateWithProduct))
        presenter?.loadLoanApplicationTemplateByProduct(1, loanState)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showLoanTemplateByProduct(loanTemplateWithProduct)
        Mockito.verify(view, Mockito.never())?.showError(context?.getString(R.string.error_fetching_template))
    }

    @Test
    fun testLoadLoanApplicationTemplateByProductUpdate() {
        val loanState = LoanState.UPDATE
        Mockito.`when`(dataManager?.getLoanTemplateByProduct(1)).thenReturn(Observable.just(loanTemplateWithProduct))
        presenter?.loadLoanApplicationTemplateByProduct(1, loanState)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showUpdateLoanTemplateByProduct(loanTemplateWithProduct)
        Mockito.verify(view, Mockito.never())?.showError(context?.getString(R.string.error_fetching_template))
    }

    @Test
    fun testLoadLoanApplicationTemplateByProductFails() {
        val loanState = LoanState.CREATE
        Mockito.`when`(dataManager?.getLoanTemplateByProduct(1)).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadLoanApplicationTemplateByProduct(1, loanState)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(context?.getString(R.string.error_fetching_template))
        Mockito.verify(view, Mockito.never())?.showLoanTemplate(null)
    } //    @Test
    //    public void testCreateLoanAccount() {
    //        when(dataManager.createLoansAccount(loansPayload)).thenReturn(Observable.
    //                just(mockedResponseBody));
    //
    //        presenter.createLoansAccount(loansPayload);
    //
    //        verify(view)?.showProgress();
    //        verify(view)?.hideProgress();
    //        verify(view)?.showLoanAccountCreatedSuccessfully();
    //
    //    }
    //
    //    @Test
    //    public void testUpdateLoanAccount() {
    //        when(dataManager.updateLoanAccount(1, loansPayload)).thenReturn(Observable.
    //                just(mockedResponseBody));
    //
    //        presenter.updateLoanAccount(1, loansPayload);
    //
    //        verify(view)?.showProgress();
    //        verify(view)?.hideProgress();
    //        verify(view)?.showLoanAccountUpdatedSuccessfully();
    //
    //    }
}
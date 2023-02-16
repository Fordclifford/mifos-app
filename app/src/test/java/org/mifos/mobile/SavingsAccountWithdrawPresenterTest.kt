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
import org.lspl.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.lspl.mobile.presenters.SavingsAccountWithdrawPresenter
import org.lspl.mobile.ui.views.SavingsAccountWithdrawView
import org.lspl.mobile.util.RxSchedulersOverrideRule

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SavingsAccountWithdrawPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: SavingsAccountWithdrawView? = null
    private var presenter: SavingsAccountWithdrawPresenter? = null

    @Mock
    var payload: SavingsAccountWithdrawPayload? = null

    @Mock
    var responseBody: ResponseBody? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = SavingsAccountWithdrawPresenter(dataManager!!, context!!)
        presenter?.attachView(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testSubmitWithdrawSavingsAccount() {
        val accountId = "1"
        Mockito.`when`(dataManager?.submitWithdrawSavingsAccount(accountId, payload))
                .thenReturn(Observable.just(responseBody))
        presenter?.submitWithdrawSavingsAccount(accountId, payload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showSavingsAccountWithdrawSuccessfully()
    }

    @Test
    fun testSubmitWithdrawSavingsAccountOnError() {
        val accountId = "1"
        val exception = Exception("message")
        Mockito.`when`(dataManager?.submitWithdrawSavingsAccount(accountId, payload))
                .thenReturn(Observable.error(exception))
        presenter?.submitWithdrawSavingsAccount(accountId, payload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(exception.message)
    }
}
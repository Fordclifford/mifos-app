package org.lspl.mobile

import android.content.Context

import io.reactivex.Observable

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.lspl.mobile.api.DataManager
import org.lspl.mobile.models.accounts.savings.SavingsWithAssociations
import org.lspl.mobile.presenters.SavingAccountsDetailPresenter
import org.lspl.mobile.ui.views.SavingAccountsDetailView
import org.lspl.mobile.util.RxSchedulersOverrideRule
import org.lspl.mobile.utils.Constants

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 24/7/17.
 */
@RunWith(MockitoJUnitRunner::class)
class SavingAccountDetailPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: SavingAccountsDetailView? = null
    private var savingsWithAssociations: SavingsWithAssociations? = null
    private var presenter: SavingAccountsDetailPresenter? = null

    @Before
    fun setUp() {
        presenter = SavingAccountsDetailPresenter(dataManager!!, context)
        presenter?.attachView(view)
        savingsWithAssociations = FakeRemoteDataSource.savingsWithAssociations
    }

    @Test
    fun testLoadSavingsWithAssociations() {
        Mockito.`when`(dataManager?.getSavingsWithAssociations(1, Constants.TRANSACTIONS)).thenReturn(
                Observable.just(savingsWithAssociations))
        presenter?.loadSavingsWithAssociations(1)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showSavingAccountsDetail(savingsWithAssociations)
        Mockito.verify(view, Mockito.never())?.showErrorFetchingSavingAccountsDetail(context?.getString(R.string.error_saving_account_details_loading))
    }

    @Test
    fun testLoadSavingsWithAssociationsFails() {
        Mockito.`when`(dataManager?.getSavingsWithAssociations(1, Constants.TRANSACTIONS)).thenReturn(
                Observable.error(RuntimeException()))
        presenter?.loadSavingsWithAssociations(1)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showErrorFetchingSavingAccountsDetail(context?.getString(R.string.error_saving_account_details_loading))
        Mockito.verify(view, Mockito.never())?.showSavingAccountsDetail(savingsWithAssociations)
    }
}
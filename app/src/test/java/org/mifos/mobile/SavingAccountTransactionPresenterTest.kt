package org.lspl.mobile

import android.content.Context

import io.reactivex.Observable

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.lspl.mobile.api.DataManager
import org.lspl.mobile.models.accounts.savings.SavingsWithAssociations
import org.lspl.mobile.presenters.SavingAccountsTransactionPresenter
import org.lspl.mobile.ui.views.SavingAccountsTransactionView
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
 * Created by dilpreet on 24/7/17.
 */
@RunWith(MockitoJUnitRunner::class)
class SavingAccountTransactionPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: SavingAccountsTransactionView? = null
    private var savingsWithAssociations: SavingsWithAssociations? = null
    private var presenter: SavingAccountsTransactionPresenter? = null

    @Before
    fun setUp() {
        presenter = SavingAccountsTransactionPresenter(dataManager!!, context)
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
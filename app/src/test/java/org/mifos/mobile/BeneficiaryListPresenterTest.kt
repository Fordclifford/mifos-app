package org.lspl.mobile

import android.content.Context
import io.reactivex.Observable

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.lspl.mobile.api.DataManager
import org.lspl.mobile.models.beneficiary.Beneficiary
import org.lspl.mobile.presenters.BeneficiaryListPresenter
import org.lspl.mobile.ui.views.BeneficiariesView
import org.lspl.mobile.util.RxSchedulersOverrideRule
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
class BeneficiaryListPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: BeneficiariesView? = null
    private var presenter: BeneficiaryListPresenter? = null
    private var beneficiaryList: List<Beneficiary?>? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = BeneficiaryListPresenter(dataManager!!, context!!)
        presenter?.attachView(view)
        beneficiaryList = FakeRemoteDataSource.beneficiaries
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    @Throws(Exception::class)
    fun testLoadBeneficiaries() {
        Mockito.`when`(dataManager?.beneficiaryList).thenReturn(Observable.just(beneficiaryList))
        presenter?.loadBeneficiaries()
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showBeneficiaryList(beneficiaryList)
        Mockito.verify(view, Mockito.never())?.showError(context?.getString(R.string.error_fetching_beneficiaries))
    }

    @Test
    @Throws(Exception::class)
    fun testLoadBeneficiariesFails() {
        Mockito.`when`(dataManager?.beneficiaryList).thenReturn(Observable.error(RuntimeException()))
        presenter?.loadBeneficiaries()
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showError(context?.getString(R.string.error_fetching_beneficiaries))
        Mockito.verify(view, Mockito.never())?.showBeneficiaryList(beneficiaryList)
    }
}
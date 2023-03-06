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
import org.lspl.mobile.models.register.RegisterPayload
import org.lspl.mobile.presenters.RegistrationPresenter
import org.lspl.mobile.ui.views.RegistrationView
import org.lspl.mobile.util.RxSchedulersOverrideRule
<<<<<<< HEAD
=======
import org.mifos.mobile.FakeRemoteDataSource
>>>>>>> 1fea8c461ffd9e5ad3acc64efd9af8145187cd91

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Chirag Gupta on 11/29/17.
 */
@RunWith(MockitoJUnitRunner::class)
class RegistrationPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: RegistrationView? = null

    @Mock
    var responseBody: ResponseBody? = null
    private var presenter: RegistrationPresenter? = null
    private var registerPayload: RegisterPayload? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = RegistrationPresenter(dataManager!!, context)
        presenter?.attachView(view!!)
        registerPayload = FakeRemoteDataSource.registerPayload
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    @Throws(Exception::class)
    fun testRegisterUser() {
        Mockito.`when`<Observable<ResponseBody?>?>(dataManager?.registerUser(registerPayload)).thenReturn(Observable.just(responseBody))
        presenter?.registerUser(registerPayload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showRegisteredSuccessfully(clientId)
        Mockito.verify(view, Mockito.never())?.showError("")
    }

    @Test
    @Throws(Exception::class)
    fun testRegisterUserFails() {
        Mockito.`when`(dataManager?.registerUser(registerPayload)).thenReturn(Observable.error(RuntimeException()))
        presenter?.registerUser(registerPayload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view, Mockito.never())?.showRegisteredSuccessfully(clientId)
        Mockito.verify(view)?.showError("")
    }
}
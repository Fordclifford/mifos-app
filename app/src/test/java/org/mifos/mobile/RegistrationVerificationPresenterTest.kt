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
import org.lspl.mobile.models.register.UserVerify
import org.lspl.mobile.presenters.RegistrationVerificationPresenter
import org.lspl.mobile.ui.views.RegistrationVerificationView
import org.lspl.mobile.util.RxSchedulersOverrideRule

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Sean Kelly on 9/12/17.
 */
@RunWith(MockitoJUnitRunner::class)
class RegistrationVerificationPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: RegistrationVerificationView? = null

    @Mock
    var responseBody: ResponseBody? = null
    private var presenter: RegistrationVerificationPresenter? = null
    private var userVerify: UserVerify? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = RegistrationVerificationPresenter(dataManager!!, context!!)
        presenter?.attachView(view)
        userVerify = FakeRemoteDataSource.userVerify
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    @Throws(Exception::class)
    fun testVerifyUser() {
        Mockito.`when`<Observable<ResponseBody?>?>(dataManager?.verifyUser(userVerify)).thenReturn(Observable.just(responseBody))
        presenter?.verifyUser(userVerify)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showVerifiedSuccessfully()
        Mockito.verify(view, Mockito.never())?.showError("")
    }

    @Test
    @Throws(Exception::class)
    fun testVerifyUserFails() {
        Mockito.`when`(dataManager?.verifyUser(userVerify)).thenReturn(Observable.error(RuntimeException()))
        presenter?.verifyUser(userVerify)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view, Mockito.never())?.showVerifiedSuccessfully()
        Mockito.verify(view)?.showError("")
    }
}
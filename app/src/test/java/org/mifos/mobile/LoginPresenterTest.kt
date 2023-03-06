package org.lspl.mobile

import android.content.Context

import io.reactivex.Observable

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.lspl.mobile.api.BaseURL
import org.lspl.mobile.api.DataManager
import org.lspl.mobile.api.local.PreferencesHelper
import org.lspl.mobile.models.Page
import org.lspl.mobile.models.User
import org.lspl.mobile.models.client.Client
import org.lspl.mobile.models.payload.LoginPayload
import org.lspl.mobile.presenters.LoginPresenter
import org.lspl.mobile.ui.views.LoginView
import org.lspl.mobile.util.RxSchedulersOverrideRule
<<<<<<< HEAD
=======
import org.mifos.mobile.FakeRemoteDataSource
import org.mifos.mobile.RetrofitUtils
>>>>>>> 1fea8c461ffd9e5ad3acc64efd9af8145187cd91

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by dilpreet on 27/6/17.
 */
@RunWith(MockitoJUnitRunner::class)
class LoginPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var mockHelper: PreferencesHelper? = null

    @Mock
    var view: LoginView? = null
    private var presenter: LoginPresenter? = null
    private var user: User? = null
    private var clientPage: Page<Client?>? = null
    private var noClientPage: Page<Client?>? = null
    private var loginPayload: LoginPayload? = null
    private var emptyClientName: String = ""

    @Before
    @Throws(Exception::class)
    fun setUp() {
        Mockito.`when`(mockHelper?.baseUrl).thenReturn(BaseURL.PROTOCOL_HTTPS + BaseURL.API_ENDPOINT)
        Mockito.`when`(dataManager?.preferencesHelper).thenReturn(mockHelper)
        presenter = LoginPresenter(dataManager, context)
        presenter?.attachView(view)
        user = FakeRemoteDataSource.user
        clientPage = FakeRemoteDataSource.clients
        noClientPage = FakeRemoteDataSource.noClients
        loginPayload = FakeRemoteDataSource.loginPayload
    }

    @Test
    @Throws(Exception::class)
    fun testLogin() {
        Mockito.`when`(dataManager?.login(loginPayload)).thenReturn(Observable.just(user))
        presenter?.login(loginPayload)
        Mockito.verify(view)?.showProgress()
        Mockito.verify(view)?.onLoginSuccess()
    }

    @Test
    @Throws(Exception::class)
    fun testLoadClients() {
        val clientName: String? = clientPage?.pageItems?.get(0)?.displayName
        Mockito.`when`(dataManager?.clients).thenReturn(Observable.just(clientPage))
        presenter?.loadClient()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showPassCodeActivity(clientName)
        Mockito.verify(view, Mockito.never())?.showMessage(context?.getString(R.string.error_fetching_client))
    }

    @Test
    @Throws(Exception::class)
    fun testLoadNoClients() {
        Mockito.`when`(dataManager?.clients).thenReturn(Observable.just(noClientPage))
        presenter?.loadClient()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showMessage(context?.getString(R.string.error_client_not_found))
        Mockito.verify(view, Mockito.never())?.showPassCodeActivity(emptyClientName)
    }

    @Test
    @Throws(Exception::class)
    fun testLoadClientFails() {
        Mockito.`when`(dataManager?.clients).thenReturn(Observable.error(RetrofitUtils.get404Exception()))
        presenter?.loadClient()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showMessage(context?.getString(R.string.error_fetching_client))
        Mockito.verify(view, Mockito.never())?.showPassCodeActivity(emptyClientName)
    }

    @Test
    @Throws(Exception::class)
    fun testLoadClientUnauthorized() {
        Mockito.`when`(dataManager?.clients).thenReturn(Observable.error(RetrofitUtils.get401Exception()))
        presenter?.loadClient()
        Mockito.verify(view)?.hideProgress()
        Mockito.verify(view)?.showMessage(context?.getString(R.string.unauthorized_client))
        Mockito.verify(view, Mockito.never())?.showPassCodeActivity(emptyClientName)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }
}
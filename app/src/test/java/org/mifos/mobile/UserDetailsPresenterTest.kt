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
import org.lspl.mobile.api.local.PreferencesHelper
import org.lspl.mobile.models.client.Client
import org.lspl.mobile.models.notification.NotificationRegisterPayload
import org.lspl.mobile.presenters.UserDetailsPresenter
import org.lspl.mobile.ui.views.UserDetailsView
import org.lspl.mobile.util.RxSchedulersOverrideRule
<<<<<<< HEAD
=======
import org.mifos.mobile.FakeRemoteDataSource
>>>>>>> 1fea8c461ffd9e5ad3acc64efd9af8145187cd91

import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserDetailsPresenterTest {
    @Rule
    @JvmField
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    var context: Context? = null

    @Mock
    var dataManager: DataManager? = null

    @Mock
    var view: UserDetailsView? = null

    @Mock
    var preferencesHelper: PreferencesHelper? = null

    @Mock
    var responseBody: ResponseBody? = null
    private var presenter: UserDetailsPresenter? = null
    private var client: Client? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        presenter = UserDetailsPresenter(context!!, dataManager!!, preferencesHelper!!)
        client = FakeRemoteDataSource.currentClient
        presenter?.attachView(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        presenter?.detachView()
    }

    @Test
    fun testGetUserDetails() {
        Mockito.`when`(dataManager?.currentClient)
                .thenReturn(Observable.just(client))
        presenter?.userDetails
        Mockito.verify(view)?.showUserDetails(client)
    }

    @Test
    fun testGetUserDetailsFails() {
        Mockito.`when`(dataManager?.currentClient)
                .thenReturn(Observable.error(Throwable()))
        presenter?.userDetails
        Mockito.verify(view)?.showError(context
                ?.getString(R.string.error_client_not_found))
    }

    @Test
    fun testRegisterNotification() {
        val token = "1"
        Mockito.`when`<Observable<ResponseBody?>?>(dataManager?.registerNotification(ArgumentMatchers.any(NotificationRegisterPayload::class.java)))
                .thenReturn(Observable.just(responseBody))
        presenter?.registerNotification(token)
        Mockito.verify(preferencesHelper)?.setSentTokenToServer(true)
        Mockito.verify(preferencesHelper)?.saveGcmToken(token)
    }
}
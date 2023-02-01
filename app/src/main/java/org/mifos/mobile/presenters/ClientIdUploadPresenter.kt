package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import okhttp3.ResponseBody

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.register.IdentifierPayload
import org.mifos.mobile.models.register.RegisterPayload
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.ClientIdUploadView
import org.mifos.mobile.ui.views.PassportUploadView
import org.mifos.mobile.ui.views.RegistrationView
import org.mifos.mobile.utils.MFErrorParser

import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
class ClientIdUploadPresenter @Inject constructor(
    private val dataManager: DataManager?,
    @ApplicationContext context: Context?
) : BasePresenter<ClientIdUploadView?>(context) {
    private val compositeDisposables: CompositeDisposable = CompositeDisposable()
    fun attachView(mvpView: ClientIdUploadView) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposables.clear()
    }
    fun uploadPhoto(){

    }

}
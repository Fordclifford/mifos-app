package org.enkasacco.mobile.presenters

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.enkasacco.mobile.api.DataManager
import org.enkasacco.mobile.injection.ApplicationContext
import org.enkasacco.mobile.models.stkpush.StkpushResponse
import org.enkasacco.mobile.models.stkpush.StkpushStatusResponse
import org.enkasacco.mobile.models.stkpush.StkpushRequestPayload
import org.enkasacco.mobile.models.stkpush.StkpushStatusRequest
import org.enkasacco.mobile.presenters.base.BasePresenter
import org.enkasacco.mobile.ui.fragments.StkPushFragment
import org.enkasacco.mobile.ui.views.StkpushView
import org.enkasacco.mobile.utils.MFErrorParser
import javax.inject.Inject


/**
 * Created by dilpreet on 31/7/17.
 */
class StkpushPresenter @Inject constructor(
    private val dataManager: DataManager?,
    @ApplicationContext context: Context?
) : BasePresenter<StkpushView?>(context) {
    private val compositeDisposables: CompositeDisposable = CompositeDisposable()
    fun attachView(mvpView: StkPushFragment) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposables.clear()
    }



    fun stkPush(payload: StkpushRequestPayload) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.stkPush(payload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<StkpushResponse?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mvpView?.hideProgress()
                    mvpView?.showError(MFErrorParser.errorMessage(e))
                }
                override fun onNext(responseBody: StkpushResponse) {
                    mvpView?.hideProgress()
                    mvpView?.showSuccessfulStatus(responseBody)
                }
            })?.let { compositeDisposables.add(it) }
    }

    fun stkPushStatus(payload: StkpushStatusRequest) {
        dataManager?.stkPushStatus(payload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<StkpushStatusResponse?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mvpView?.hideProgress()
                    mvpView?.showError(MFErrorParser.errorMessage(e))
                }

                override fun onNext(responseBody: StkpushStatusResponse) {
                    mvpView?.hideProgress()
                    mvpView?.showFinalStatus(responseBody)
                }
            })?.let { compositeDisposables.add(it) }
    }

}
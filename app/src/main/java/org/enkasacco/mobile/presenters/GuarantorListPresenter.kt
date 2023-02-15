package org.enkasacco.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.enkasacco.mobile.api.DataManager

import org.enkasacco.mobile.injection.ApplicationContext
import org.enkasacco.mobile.models.guarantor.GuarantorPayload
import org.enkasacco.mobile.presenters.base.BasePresenter
import org.enkasacco.mobile.ui.views.GuarantorListView

import javax.inject.Inject

/*
* Created by saksham on 24/July/2018
*/
class GuarantorListPresenter @Inject constructor(@ApplicationContext context: Context?, var dataManager: DataManager?) :
        BasePresenter<GuarantorListView?>(context) {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    fun getGuarantorList(loanId: Long) {
        mvpView?.showProgress()
        dataManager?.getGuarantorList(loanId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<List<GuarantorPayload?>?>() {
                    override fun onNext(payload: List<GuarantorPayload?>) {
                        mvpView?.hideProgress()
                        mvpView?.showGuarantorListSuccessfully(payload)
                    }

                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(e.message ?: "")
                    }

                    override fun onComplete() {}
                })?.let { compositeDisposable.add(it) }
    }

}
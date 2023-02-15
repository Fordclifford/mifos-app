package org.lspl.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.lspl.mobile.R
import org.lspl.mobile.api.DataManager
import org.lspl.mobile.injection.ApplicationContext
import org.lspl.mobile.models.beneficiary.Beneficiary
import org.lspl.mobile.presenters.base.BasePresenter
import org.lspl.mobile.ui.views.BeneficiariesView

import javax.inject.Inject

/**
 * Created by dilpreet on 14/6/17.
 */
class BeneficiaryListPresenter @Inject constructor(private val dataManager: DataManager?, @ApplicationContext context: Context?) : BasePresenter<BeneficiariesView?>(context) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    /**
     * Used to load Beneficiaries as a [List] of [Beneficiary] from server and notifies
     * the view to display it. And in case of any error during fetching the required details it
     * notifies the view.
     */
    fun loadBeneficiaries() {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.beneficiaryList
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<List<Beneficiary?>?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(context
                                ?.getString(R.string.beneficiaries))
                    }

                    override fun onNext(beneficiaries: List<Beneficiary?>) {
                        mvpView?.hideProgress()
                        mvpView?.showBeneficiaryList(beneficiaries)
                    }
                })?.let { compositeDisposable.add(it) }
    }

}
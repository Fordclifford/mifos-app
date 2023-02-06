package org.enkasacco.mobile.presenters

import android.content.Context

import org.enkasacco.mobile.R
import org.enkasacco.mobile.injection.ApplicationContext
import org.enkasacco.mobile.models.FAQ
import org.enkasacco.mobile.presenters.base.BasePresenter
import org.enkasacco.mobile.ui.views.HelpView

import java.util.*
import javax.inject.Inject

/**
 * Created by dilpreet on 12/8/17.
 */
class HelpPresenter @Inject constructor(@ApplicationContext context: Context?) :
        BasePresenter<HelpView?>(context) {

    fun loadFaq() {
        val qs = context?.resources?.getStringArray(R.array.faq_qs)
        val ans = context?.resources?.getStringArray(R.array.faq_ans)
        val faqArrayList = ArrayList<FAQ?>()
        if (qs != null)
            for (i in qs.indices) {
                faqArrayList.add(FAQ(qs[i], ans?.get(i)))
            }
        mvpView?.showFaq(faqArrayList)
    }

    fun filterList(faqArrayList: ArrayList<FAQ?>?, query: String): ArrayList<FAQ?>? {
        val filteredList = ArrayList<FAQ?>()
        if (faqArrayList != null) {
            for (faq in faqArrayList) {
                if (faq?.question?.toLowerCase(Locale.ROOT)?.contains(query.toLowerCase(Locale.ROOT)) == true) {
                    filteredList.add(faq)
                }
            }
        }
        return filteredList
    }
}
package org.lspl.mobile.ui.views

import org.lspl.mobile.models.FAQ
import org.lspl.mobile.ui.views.base.MVPView
import java.util.*

/**
 * Created by dilpreet on 12/8/17.
 */
interface HelpView : MVPView {
    fun showFaq(faqArrayList: ArrayList<FAQ?>?)
}
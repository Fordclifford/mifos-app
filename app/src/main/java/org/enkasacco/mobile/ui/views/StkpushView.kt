package org.enkasacco.mobile.ui.views

import org.enkasacco.mobile.models.stkpush.StkpushResponse
import org.enkasacco.mobile.models.stkpush.StkpushStatusResponse
import org.enkasacco.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 31/7/17.
 */
interface StkpushView : MVPView {
    fun showError(msg: String?)
    fun showSuccessfulStatus(responseBody: StkpushResponse)
    fun showFinalStatus(responseBody: StkpushStatusResponse)
}
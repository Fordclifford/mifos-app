package org.mifos.mobile.ui.views

import org.mifos.mobile.models.stkpush.StkpushResponse
import org.mifos.mobile.models.stkpush.StkpushStatusResponse
import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 31/7/17.
 */
interface StkpushView : MVPView {
    fun showError(msg: String?)
    fun showSuccessfulStatus(responseBody: StkpushResponse)
    fun showFinalStatus(responseBody: StkpushStatusResponse)
}
package org.enkasacco.mobile.ui.views

import org.enkasacco.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 1/7/17.
 */
interface TransferProcessView : MVPView {
    fun showTransferredSuccessfully()
    fun showError(msg: String?)
}
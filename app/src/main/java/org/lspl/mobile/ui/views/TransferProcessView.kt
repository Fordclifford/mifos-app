package org.lspl.mobile.ui.views

import org.lspl.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 1/7/17.
 */
interface TransferProcessView : MVPView {
    fun showTransferredSuccessfully()
    fun showError(msg: String?)
}
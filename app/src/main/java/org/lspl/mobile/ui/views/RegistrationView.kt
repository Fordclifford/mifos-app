package org.lspl.mobile.ui.views

import org.lspl.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 31/7/17.
 */
interface RegistrationView : MVPView {
    fun showRegisteredSuccessfully(clientId: Long)
    fun showError(msg: String?)
}
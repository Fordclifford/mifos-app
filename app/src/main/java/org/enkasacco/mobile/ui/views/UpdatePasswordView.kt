package org.enkasacco.mobile.ui.views

import org.enkasacco.mobile.ui.views.base.MVPView

/*
* Created by saksham on 13/July/2018
*/   interface UpdatePasswordView : MVPView {
    fun showError(message: String?)
    fun showPasswordUpdatedSuccessfully()
}
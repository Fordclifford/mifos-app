package org.lspl.mobile.ui.views

import org.lspl.mobile.ui.views.base.MVPView

/*
* Created by saksham on 02/July/2018
*/   interface SavingsAccountWithdrawView : MVPView {
    fun showUserInterface()
    fun submitWithdrawSavingsAccount()
    fun showSavingsAccountWithdrawSuccessfully()
    fun showMessage(message: String?)
    fun showError(error: String?)
}
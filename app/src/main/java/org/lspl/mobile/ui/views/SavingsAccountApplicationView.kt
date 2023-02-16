package org.lspl.mobile.ui.views

import org.lspl.mobile.models.templates.savings.SavingsAccountTemplate
import org.lspl.mobile.ui.views.base.MVPView

/*
* Created by saksham on 30/June/2018
*/   interface SavingsAccountApplicationView : MVPView {
    fun showUserInterfaceSavingAccountApplication(template: SavingsAccountTemplate?)
    fun showSavingsAccountApplicationSuccessfully()
    fun showUserInterfaceSavingAccountUpdate(template: SavingsAccountTemplate?)
    fun showSavingsAccountUpdateSuccessfully()
    fun showError(error: String?)
    fun showMessage(showMessage: String?)
}
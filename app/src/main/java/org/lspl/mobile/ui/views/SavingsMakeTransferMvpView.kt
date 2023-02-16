package org.lspl.mobile.ui.views

import org.lspl.mobile.models.templates.account.AccountOptionsTemplate
import org.lspl.mobile.ui.views.base.MVPView

/**
 * Created by Rajan Maurya on 10/03/17.
 */
interface SavingsMakeTransferMvpView : MVPView {
    fun showUserInterface()
    fun showSavingsAccountTemplate(accountOptionsTemplate: AccountOptionsTemplate?)
    fun showToaster(message: String?)
    fun showError(message: String?)
    fun showProgressDialog()
    fun hideProgressDialog()
}
package org.lspl.mobile.ui.views

import org.lspl.mobile.models.accounts.loan.LoanAccount
import org.lspl.mobile.models.accounts.savings.SavingAccount
import org.lspl.mobile.models.accounts.share.ShareAccount
import org.lspl.mobile.ui.views.base.MVPView

/**
 * Created by Rajan Maurya on 23/10/16.
 */
interface AccountsView : MVPView {
    fun showLoanAccounts(loanAccounts: List<LoanAccount?>?)
    fun showSavingsAccounts(savingAccounts: List<SavingAccount?>?)
    fun showShareAccounts(shareAccounts: List<ShareAccount?>?)
    fun showError(errorMessage: String?)
}
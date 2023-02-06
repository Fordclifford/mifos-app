package org.enkasacco.mobile.ui.views

import org.enkasacco.mobile.models.accounts.loan.LoanAccount
import org.enkasacco.mobile.models.accounts.savings.SavingAccount
import org.enkasacco.mobile.models.accounts.share.ShareAccount
import org.enkasacco.mobile.ui.views.base.MVPView

/**
 * Created by Rajan Maurya on 23/10/16.
 */
interface AccountsView : MVPView {
    fun showLoanAccounts(loanAccounts: List<LoanAccount?>?)
    fun showSavingsAccounts(savingAccounts: List<SavingAccount?>?)
    fun showShareAccounts(shareAccounts: List<ShareAccount?>?)
    fun showError(errorMessage: String?)
}
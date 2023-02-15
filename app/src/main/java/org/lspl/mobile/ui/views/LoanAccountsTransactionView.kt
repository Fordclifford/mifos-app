package org.lspl.mobile.ui.views

import org.lspl.mobile.models.accounts.loan.LoanWithAssociations
import org.lspl.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 4/3/17.
 */
interface LoanAccountsTransactionView : MVPView {
    fun showUserInterface()
    fun showLoanTransactions(loanWithAssociations: LoanWithAssociations?)
    fun showEmptyTransactions(loanWithAssociations: LoanWithAssociations?)
    fun showErrorFetchingLoanAccountsDetail(message: String?)
}
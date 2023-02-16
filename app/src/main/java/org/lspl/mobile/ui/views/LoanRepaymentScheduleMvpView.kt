package org.lspl.mobile.ui.views

import org.lspl.mobile.models.accounts.loan.LoanWithAssociations
import org.lspl.mobile.ui.views.base.MVPView

/**
 * Created by Rajan Maurya on 03/03/17.
 */
interface LoanRepaymentScheduleMvpView : MVPView {
    fun showUserInterface()
    fun showLoanRepaymentSchedule(loanWithAssociations: LoanWithAssociations?)
    fun showEmptyRepaymentsSchedule(loanWithAssociations: LoanWithAssociations?)
    fun showError(message: String?)
}
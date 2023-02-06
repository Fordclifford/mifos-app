package org.enkasacco.mobile.ui.views

import org.enkasacco.mobile.models.accounts.loan.LoanWithAssociations
import org.enkasacco.mobile.ui.views.base.MVPView

/**
 * Created by Rajan Maurya on 03/03/17.
 */
interface LoanRepaymentScheduleMvpView : MVPView {
    fun showUserInterface()
    fun showLoanRepaymentSchedule(loanWithAssociations: LoanWithAssociations?)
    fun showEmptyRepaymentsSchedule(loanWithAssociations: LoanWithAssociations?)
    fun showError(message: String?)
}
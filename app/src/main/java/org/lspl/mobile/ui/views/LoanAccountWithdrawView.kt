package org.lspl.mobile.ui.views

import org.lspl.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 7/6/17.
 */
interface LoanAccountWithdrawView : MVPView {
    fun showLoanAccountWithdrawSuccess()
    fun showLoanAccountWithdrawError(message: String?)
}
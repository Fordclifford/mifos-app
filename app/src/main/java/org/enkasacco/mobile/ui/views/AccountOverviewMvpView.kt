package org.enkasacco.mobile.ui.views

import org.enkasacco.mobile.ui.views.base.MVPView

/**
 * @author Rajan Maurya
 * On 16/10/17.
 */
interface AccountOverviewMvpView : MVPView {
    fun showTotalLoanSavings(totalLoan: Double?, totalSavings: Double?)
    fun showError(message: String?)
}
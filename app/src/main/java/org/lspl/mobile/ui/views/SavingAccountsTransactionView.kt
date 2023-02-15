package org.lspl.mobile.ui.views

import org.lspl.mobile.models.accounts.savings.SavingsWithAssociations
import org.lspl.mobile.models.accounts.savings.Transactions
import org.lspl.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 6/3/17.
 */
interface SavingAccountsTransactionView : MVPView {
    fun showUserInterface()
    fun showSavingAccountsDetail(savingsWithAssociations: SavingsWithAssociations?)
    fun showErrorFetchingSavingAccountsDetail(message: String?)
    fun showFilteredList(list: List<Transactions?>?)
    fun showEmptyTransactions()
}
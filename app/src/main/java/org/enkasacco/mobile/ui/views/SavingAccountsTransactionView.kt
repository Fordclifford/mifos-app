package org.enkasacco.mobile.ui.views

import org.enkasacco.mobile.models.accounts.savings.SavingsWithAssociations
import org.enkasacco.mobile.models.accounts.savings.Transactions
import org.enkasacco.mobile.ui.views.base.MVPView

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
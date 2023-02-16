package org.lspl.mobile.models.accounts

import org.lspl.mobile.models.accounts.savings.SavingAccount
import java.util.*

/**
 * @author Vishwajeet
 * @since 13/08/16
 */
data class SavingAccountsListResponse (
    var savingsAccounts: List<SavingAccount> = ArrayList()
)

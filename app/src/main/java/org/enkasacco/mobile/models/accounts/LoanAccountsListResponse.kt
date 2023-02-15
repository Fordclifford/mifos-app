package org.enkasacco.mobile.models.accounts

import org.enkasacco.mobile.models.accounts.loan.LoanAccount
import java.util.*

/**
 * @author Vishwajeet
 * @since 13/08/16
 */
data class LoanAccountsListResponse (
        var loanAccounts: List<LoanAccount> = ArrayList())

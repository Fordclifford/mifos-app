package org.lspl.mobile.models

import org.lspl.mobile.models.beneficiary.Beneficiary
import org.lspl.mobile.models.templates.account.AccountOptionsTemplate

/**
 * Created by dilpreet on 23/6/17.
 */

data class AccountOptionAndBeneficiary(
        val accountOptionsTemplate: AccountOptionsTemplate,
        val beneficiaryList: List<Beneficiary?>
)

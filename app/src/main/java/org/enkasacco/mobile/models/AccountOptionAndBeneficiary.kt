package org.enkasacco.mobile.models

import org.enkasacco.mobile.models.beneficiary.Beneficiary
import org.enkasacco.mobile.models.templates.account.AccountOptionsTemplate

/**
 * Created by dilpreet on 23/6/17.
 */

data class AccountOptionAndBeneficiary(
        val accountOptionsTemplate: AccountOptionsTemplate,
        val beneficiaryList: List<Beneficiary?>
)

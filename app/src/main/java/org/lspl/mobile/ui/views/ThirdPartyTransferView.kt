package org.lspl.mobile.ui.views

import org.lspl.mobile.models.beneficiary.Beneficiary
import org.lspl.mobile.models.templates.account.AccountOptionsTemplate
import org.lspl.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 21/6/17.
 */
interface ThirdPartyTransferView : MVPView {
    fun showUserInterface()
    fun showToaster(msg: String?)
    fun showThirdPartyTransferTemplate(accountOptionsTemplate: AccountOptionsTemplate?)
    fun showBeneficiaryList(beneficiaries: List<Beneficiary?>?)
    fun showError(msg: String?)
}
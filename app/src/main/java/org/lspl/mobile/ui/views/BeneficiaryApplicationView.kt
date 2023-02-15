package org.lspl.mobile.ui.views

import org.lspl.mobile.models.templates.beneficiary.BeneficiaryTemplate
import org.lspl.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 16/6/17.
 */
interface BeneficiaryApplicationView : MVPView {
    fun showUserInterface()
    fun showBeneficiaryTemplate(beneficiaryTemplate: BeneficiaryTemplate?)
    fun showBeneficiaryCreatedSuccessfully()
    fun showBeneficiaryUpdatedSuccessfully()
    fun showError(msg: String?)
    fun setVisibility(state: Int)
}
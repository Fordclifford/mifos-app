package org.enkasacco.mobile.ui.views

import org.enkasacco.mobile.models.beneficiary.Beneficiary
import org.enkasacco.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 14/6/17.
 */
interface BeneficiariesView : MVPView {
    fun showUserInterface()
    fun showError(msg: String?)
    fun showBeneficiaryList(beneficiaryList: List<Beneficiary?>?)
}
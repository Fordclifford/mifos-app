package org.enkasacco.mobile.ui.views

import org.enkasacco.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 16/6/17.
 */
interface BeneficiaryDetailView : MVPView {
    fun showUserInterface()
    fun showBeneficiaryDeletedSuccessfully()
    fun showError(msg: String?)
}
package org.lspl.mobile.ui.views

import org.lspl.mobile.models.templates.client.FamilyMemberOptions
import org.lspl.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 31/7/17.
 */
interface RegistrationVerificationView : MVPView {
    fun showVerifiedSuccessfully()
    fun showError(msg: String?)
    fun showClientTemplate(clientsTemplate: FamilyMemberOptions?)
}
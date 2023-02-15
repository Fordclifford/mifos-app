package org.enkasacco.mobile.ui.views

import org.enkasacco.mobile.models.templates.client.FamilyMemberOptions
import org.enkasacco.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 31/7/17.
 */
interface RegistrationVerificationView : MVPView {
    fun showVerifiedSuccessfully()
    fun showError(msg: String?)
    fun showClientTemplate(clientsTemplate: FamilyMemberOptions?)
}
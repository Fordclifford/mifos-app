package org.lspl.mobile.models.passwordreset

/**
 * Created by dilpreet on 31/7/17.
 */

data class NewpasswordPayload(
        var username: String? = null,
        var newPassword: String? = null,
        var securityQuestionAnswers: List<PasswordPayload>? = null
)

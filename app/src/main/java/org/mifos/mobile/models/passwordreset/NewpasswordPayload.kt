package org.mifos.mobile.models.passwordreset

/**
 * Created by dilpreet on 31/7/17.
 */

data class NewpasswordPayload(
        var token: String? = null,
        var newPassword: String? = null
)

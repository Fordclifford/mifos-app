package org.lspl.mobile.models.passwordreset

/**
 * Created by dilpreet on 31/7/17.
 */

data class PasswordPayload(
        var questionId: Int? = 0,
        var answer: String? = null,
)

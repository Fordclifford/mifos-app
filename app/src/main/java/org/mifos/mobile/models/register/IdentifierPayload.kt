package org.mifos.mobile.models.register

/**
 * Created by dilpreet on 31/7/17.
 */

data class IdentifierPayload(
    var documentTypeId: String? = null,
    var documentKey: String? = null,
    var description: String? = "Submited for verification",
    var status: String? = "active"
)

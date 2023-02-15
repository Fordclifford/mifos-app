package org.enkasacco.mobile.models.client

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NextOfKinPayload(
    internal var locale: String = "en",
    var name: String? = null,
    var relationshipId: Int? = null,
    var firstName: String? = null,
    var middleName: String? = null,
    var lastName: String? = null,
    var mobileNumber: String? = null,
    var genderId: Int? = null,
    var professionId: Int? = null,
    var maritalStatusId: Int? = null,
    var isDependent: Boolean? = false,
    var dateFormat: String? = "dd MMMM yyyy",
    var dateOfBirth: String? = null,
    var qualification: String? = null
) : Parcelable {
}

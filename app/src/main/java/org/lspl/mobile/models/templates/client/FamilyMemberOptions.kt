package org.lspl.mobile.models.templates.client

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */ /**
 * Created by rajan on 13/3/16.
 */
@Parcelize
data class FamilyMemberOptions(
    var genderIdOptions: List<Options>? = null,
    var maritalStatusIdOptions: List<Options>? = null,
    var professionIdOptions: List<Options>? = null,
    var relationshipIdOptions: List<Options>? = null
) : Parcelable
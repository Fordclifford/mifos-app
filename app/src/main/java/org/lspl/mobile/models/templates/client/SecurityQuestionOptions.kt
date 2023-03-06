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
data class SecurityQuestionOptions(
    var id: Int = 0,
    var name: String? = null,
    var position: Int = 0,
    var description: String? = null,
    var active: Boolean = false,
    var mandatory: Boolean = false,
) : Parcelable
package org.enkasacco.mobile.models.stkpush

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StkpushStatusRequest(
    var merchantRequestId: String? = null,
    var checkoutRequestId: String? = null,
    var responseCode: String? = null,
    var responseDescription: String? = null,
    var customerMessage: String? = null,
) : Parcelable



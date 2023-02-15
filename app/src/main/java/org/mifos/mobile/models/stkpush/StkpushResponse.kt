package org.mifos.mobile.models.stkpush

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StkpushResponse(
    var MerchantRequestID: String? = null,
    var CheckoutRequestID: String? = null,
    var ResponseCode: String? = null,
    var ResponseDescription: String? = null,
    var CustomerMessage: String? = null,
) : Parcelable



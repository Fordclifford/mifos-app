package org.mifos.mobile.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StkpushStatusResponse(
    var MerchantRequestID: String? = null,
    var CheckoutRequestID: String? = null,
    var ResponseCode: String? = null,
    var ResponseDescription: String? = null,
    var CustomerMessage: String? = null,
    var ResultCode: String? = null,
    var ResultDesc: String? = null,
) : Parcelable



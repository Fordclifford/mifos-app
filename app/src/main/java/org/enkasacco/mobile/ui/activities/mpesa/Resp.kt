package org.enkasacco.mobile.ui.activities.mpesa

data class Resp(
    var MerchantRequestID: String?,
    var CheckoutRequestID: String?,
    var ResponseCode: String?,
    var ResponseDescription: String?,
    var CustomerMessage: String?,
    var success: Boolean?,
    var message: String?,
)
package org.mifos.mobile.models.stkpush;
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize;

@Parcelize
data class StkpushRequestPayload(
        var phone: Long?=null,
        var amount: String? = null,
        var accountReference: String?=null,
): Parcelable
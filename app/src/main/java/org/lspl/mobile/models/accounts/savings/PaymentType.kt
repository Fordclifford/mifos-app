package org.lspl.mobile.models.accounts.savings

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 05/03/17.
 */

@Parcelize
data class PaymentType(
        var id: Int? = null,

        var name: String? = null
) : Parcelable
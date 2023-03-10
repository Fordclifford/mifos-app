package org.lspl.mobile.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.lspl.mobile.models.client.Currency
import org.lspl.mobile.models.client.Type
import java.util.*

/**
 * @author Vishwajeet
 * @since 10/8/16.
 */

@Parcelize
data class Transaction(

        var id: Long? = null,

        var officeId: Long? = null,

        var officeName: String? = null,

        var type: Type,

        var date: List<Int> = ArrayList(),

        var currency: Currency? = null,

        var amount: Double? = null,

        var submittedOnDate: List<Int> = ArrayList(),

        var reversed: Boolean? = null

) : Parcelable
package org.lspl.mobile.models.templates.loans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepaymentFrequencyDaysOfWeekTypeOptions(

        var id: Int? = null,

        var code: String? = null,

        var value: String? = null
) : Parcelable
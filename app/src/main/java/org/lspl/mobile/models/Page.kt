package org.lspl.mobile.models

import java.util.*

/**
 * @author Rajan Maurya
 */
data class Page<T> (

    var totalFilteredRecords: Int = 0,
    var pageItems: List<T> = ArrayList())
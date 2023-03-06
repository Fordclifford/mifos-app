package org.lspl.mobile.models

import java.util.*

/**
 * @author Vishwajeet
 * @since 12/06/16
 */

data class Question (

    var id: Int = 0,
    var userId: Int = 0,
    var answer: String? = null,
    var question: Int? = 0,
    var new: Boolean = true
    )

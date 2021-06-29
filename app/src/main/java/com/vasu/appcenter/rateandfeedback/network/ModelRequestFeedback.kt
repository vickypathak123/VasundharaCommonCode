package com.vasu.appcenter.rateandfeedback.network

import androidx.annotation.Keep

/**
 * ModelFeedback.kt - Data class of feedback.
 * @author:  Jignesh N Patel
 * @date: 10-Feb-2021 9:35 AM
 */

@Keep
data class ModelRequestFeedback(
    var package_name: String,
    var review: String,
    var ratings: String,
    var files: ArrayList<String>,
    var contact_information: String,
    var version_code: String,
    var version_name: String
)

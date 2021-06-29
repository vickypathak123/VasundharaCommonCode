package com.vasu.appcenter.rateandfeedback.network

import androidx.annotation.Keep

@Keep
data class ModelResponseFeedback(
    var ResponseCode: Int? = null,
    var ResponseMessage: String? = null
)

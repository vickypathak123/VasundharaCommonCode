package com.vasu.appcenter.retrofit.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class ForceUpdateModel(
    @SerializedName("is_need_to_update")
    @Expose val is_need_to_update: Boolean = false,
    @SerializedName("message")
    @Expose val message: String = "",
    @SerializedName("status")
    @Expose val status: Boolean = false
)
package com.vasu.appcenter.akshay.api

import androidx.annotation.NonNull
import com.vasu.appcenter.retrofit.model.ForceUpdateModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIInterface {

    @FormUrlEncoded
    @POST(INTERFACE_NAME_UPDATE_APK_VERSION)
    suspend fun getForceUpdateStatus(
        @NonNull @Field(FIELD_NAME_PACKAGE_NAME) fPackageName: String,
        @NonNull @Field(FIELD_NAME_VERSION_CODE) fVersionCode: Double
    ): ForceUpdateModel

    /*@POST(API_VERSION_INTERFACE_NAME)
    suspend fun getApiVersionResponse(): APIVersionModel*/

}
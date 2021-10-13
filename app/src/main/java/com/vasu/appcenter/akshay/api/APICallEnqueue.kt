package com.vasu.appcenter.akshay.api

import android.content.Context
import androidx.annotation.NonNull
import com.example.appcenter.utilities.isSDKBelow21
import com.vasu.appcenter.akshay.api.APIBuilder.getUpdateBaseUrl
import com.vasu.appcenter.akshay.api.APIBuilder.getUpdateClient
import com.vasu.appcenter.retrofit.model.ForceUpdateModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

object APICallEnqueue {

    suspend fun Context.getForceUpdateStatus(
        @NonNull fJob: Job,
        @NonNull fPackageName: String,
        @NonNull fVersionCode: Double,
        @NonNull fListener: APIResponseListener<ForceUpdateModel>,
    ) {
        if (isSDKBelow21()) {
            withContext(Dispatchers.IO + fJob) {

                val apiParams: HashMap<String, String> = HashMap()
                apiParams[FIELD_NAME_PACKAGE_NAME] = fPackageName
                apiParams[FIELD_NAME_VERSION_CODE] = fVersionCode.toString()

                GetJsonResponseCoroutineTask(
                        context = this@getForceUpdateStatus,
                        job = fJob,
                        fModelClass = ForceUpdateModel::class.java,
                        fMethodType = APIMethodType.POST,
                        fListener = fListener
                ).execute(
                        withAuthorization = true,
                        apiParams = apiParams,
                        params = arrayOf(this@getForceUpdateStatus.getUpdateBaseUrl, INTERFACE_NAME_UPDATE_APK_VERSION)
                )
            }
        } else {
            try {
                val response = withContext(Dispatchers.IO + fJob) {
                    this@getForceUpdateStatus.getUpdateClient.getForceUpdateStatus(
                        fPackageName = fPackageName,
                        fVersionCode = fVersionCode
                    )
                }
                withContext(Dispatchers.Main + fJob) {
                    fListener.onSuccess(response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main + fJob) {
                    fListener.onError(e.message)
                }
            }
        }
    }
}
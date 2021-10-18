package com.example.latest.vasu.newappcenter.newAPI

import android.content.Context
import com.example.latest.vasu.newappcenter.model.MoreAppMainModel
import com.example.latest.vasu.newappcenter.newAPI.APIBuilder.getMainBaseUrl
import com.example.latest.vasu.newappcenter.newAPI.APIBuilder.getMainClient
import com.example.latest.vasu.newappcenter.utils.PKG_NAME
import com.example.latest.vasu.newappcenter.utils.isSDKBelow21
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

object APICallEnqueue {

    suspend fun Context.getMoreAppResponse(
        fJob: Job,
        fListener: APIResponseListener<MoreAppMainModel>,
    ) {
        if (isSDKBelow21) {
            withContext(Dispatchers.IO + fJob) {

                GetJsonResponseCoroutineTask(
                        context = this@getMoreAppResponse,
                        job = fJob,
                        fModelClass = MoreAppMainModel::class.java,
                        fMethodType = APIMethodType.GET,
                        fListener = fListener
                ).execute(
                        withAuthorization = false,
                        params = arrayOf("${this@getMoreAppResponse.getMainBaseUrl}$PKG_NAME")
                )
            }
        } else {
            try {
                val response = withContext(fJob + Dispatchers.IO) {
                    this@getMoreAppResponse.getMainClient.getMoreAppResponse(PKG_NAME)
                }
                fListener.onSuccess(response)
            } catch (e: Exception) {
                withContext(fJob + Dispatchers.Main) {
                    fListener.onError(e.message)
                }
            }
        }
    }
}
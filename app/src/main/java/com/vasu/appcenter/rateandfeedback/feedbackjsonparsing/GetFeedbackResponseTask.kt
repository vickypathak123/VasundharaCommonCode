package com.vasu.appcenter.rateandfeedback.feedbackjsonparsing

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.example.appcenter.jsonparsing.JsonParserCallback
import com.vasu.appcenter.rateandfeedback.network.ModelRequestFeedback
import com.vasu.appcenter.utilities.getReviewBaseUrl
import java.io.File
import java.io.IOException

/**
 * Async task class to get json by making HTTP call
 */
class GetFeedbackResponseTask(val mContext: Context, val feedback: ModelRequestFeedback, private val callback: JsonParserCallback) : AsyncTask<Void?, Void?, Boolean>() {
    private val TAG = GetFeedbackResponseTask::class.java.simpleName
    private var response = ""


    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        if (result) {
            callback.onSuccess(response)
        } else {
            callback.onFailure(response)
        }
    }

    override fun doInBackground(vararg params: Void?): Boolean {
        // Making a request to url and getting response
        val charset = "UTF-8"
        val requestURL = mContext.getReviewBaseUrl() + "app_review"
        // val multipart = MultipartUtilityV2(requestURL, charset)


        try {

            val multipart = MultipartUtility(requestURL, charset)
            // val multipart = MultipartUtilityV2(requestURL)
            multipart.addFormField("package_name", feedback.package_name)
            multipart.addFormField("review", feedback.review)
            multipart.addFormField("ratings", feedback.ratings)
            multipart.addFormField("contact_information", feedback.contact_information)
            multipart.addFormField("version_code", feedback.version_code)
            multipart.addFormField("version_name", feedback.version_name)
            var counter = 0
            for (file in feedback.files) {
                counter += 1
                multipart.addFilePart("image[$counter]", File(file))
            }


            val multiResponse = multipart.finish()
            Log.w(TAG, "SERVER REPLIED:")
            var jsonStr = ""
            for (line in multiResponse) {
                jsonStr += line.toString()
            }

            //    val jsonStr = multipart.finish() // response from server.
            Log.i(TAG, "Response from url: $jsonStr")
            response = jsonStr
            return true
        } catch (e: Exception) {
            response = e.toString()
            Log.e(TAG, "Exception: $response")
        } catch (e1: IOException) {
            response = e1.toString()
            Log.e(TAG, "IOException: $response")
        }
        return false
    }
}
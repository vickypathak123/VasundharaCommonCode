package com.example.appcenter.jsonparsing

import android.os.AsyncTask
import android.util.Log

/**
 * Async task class to get json by making HTTP call
 */
class GetJsonResponseTask(private val callback: JsonParserCallback) : AsyncTask<String?, Void?, Boolean>() {
    private val TAG = GetJsonResponseTask::class.java.simpleName
    private var response = ""

    override fun doInBackground(vararg params: String?): Boolean {
        val sh = HttpHandler()
        // Making a request to url and getting response
        val jsonStr = sh.makeServiceCall(params[0])
        try {
            Log.i(TAG, "Response from url: $jsonStr")
            if (jsonStr != null) {
                response = jsonStr
            } else {
                response = "Couldn't get json from server."
                Log.e(TAG, response)
            }
            return true
        } catch (e: Exception) {
            response = e.toString()
            Log.e(TAG, response)
        }
        return false
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        if (result) {
            callback.onSuccess(response)
        } else {
            callback.onFailure(response)
        }
    }
}
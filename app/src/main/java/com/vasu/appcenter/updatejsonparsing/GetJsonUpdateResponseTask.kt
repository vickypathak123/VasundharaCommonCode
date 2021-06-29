package com.vasu.appcenter.updatejsonparsing

import android.os.AsyncTask
import android.util.Log
import com.example.appcenter.jsonparsing.JsonParserCallback

/**
 * Async task class to get json by making HTTP call
 */
class GetJsonUpdateResponseTask(private val callback: JsonParserCallback) : AsyncTask<String?, Void?, Boolean>() {
    private val TAG = GetJsonUpdateResponseTask::class.java.simpleName
    private var response = ""

    override fun doInBackground(vararg params: String?): Boolean {

        // Making a request to url and getting response
        val url = params[0]!!
        val pkgName = params[1]!!
        val version = params[2]!!.toDouble()
        val jsonStr = makeUpdateCall(url, pkgName, version)
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
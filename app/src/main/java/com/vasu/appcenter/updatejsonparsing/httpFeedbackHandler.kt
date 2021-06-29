package com.vasu.appcenter.updatejsonparsing

import android.util.Log
import com.example.appcenter.jsonparsing.convertStreamToString
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.*

class HttpUpdateHandler

private val TAG = HttpUpdateHandler::class.java.simpleName

fun makeUpdateCall(reqUrl: String, pkgName: String, version: Double): String? {
    var response: String? = null
    try {
        val params: MutableMap<String, Any> = LinkedHashMap()
        params["packageName"] = pkgName
        params["versionCode"] = version
        val postData = java.lang.StringBuilder()
        for ((key, value) in params.entries) {
            if (postData.isNotEmpty()) postData.append('&')
            postData.append(URLEncoder.encode(key, "UTF-8"))
            postData.append('=')
            postData.append(URLEncoder.encode(value.toString(), "UTF-8"))
            Log.i("PARAMS", URLEncoder.encode(key, "UTF-8") + ":" + URLEncoder.encode(value.toString(), "UTF-8"))
        }
        val postDataBytes = postData.toString().toByteArray(charset("UTF-8"))

        val url = URL(reqUrl)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", postDataBytes.size.toString())
        conn.doOutput = true
        conn.outputStream.write(postDataBytes)
        // read the response
        val inputStream: InputStream = BufferedInputStream(conn.inputStream)
        response = convertStreamToString(inputStream)
    } catch (e: MalformedURLException) {
        Log.e(TAG, "MalformedURLException: " + e.message)
        response = e.toString()
    } catch (e: ProtocolException) {
        Log.e(TAG, "ProtocolException: " + e.message)
        response = e.toString()
    } catch (e: IOException) {
        Log.e(TAG, "IOException: " + e.message)
        response = e.toString()
    } catch (e: Exception) {
        Log.e(TAG, "Exception: " + e.message)
        response = e.toString()
    }
    return response
}



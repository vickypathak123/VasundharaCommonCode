package com.example.appcenter.jsonparsing

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object HttpUtility {
    const val METHOD_GET = 0 // METHOD GET
    const val METHOD_POST = 1 // METHOD POST

    // static method
    fun reqPost(web_url: String, method: Int, params: HashMap<String, String>, callback: Callback?) {

        // thread for handling async task
        Thread {
            try {
                var url = web_url
                // write GET params,append with url
                if (method == METHOD_GET) {
                    for ((key1, value1) in params) {
                        val key = URLEncoder.encode(key1, "UTF-8")
                        val value = URLEncoder.encode(value1, "UTF-8")
                        url += if (!url.contains("?")) {
                            "?$key=$value"
                        } else {
                            "&$key=$value"
                        }
                    }
                }
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                urlConnection.useCaches = false
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded") // handle url encoded form data
                urlConnection.setRequestProperty("charset", "utf-8")
                if (method == METHOD_GET) {
                    urlConnection.requestMethod = "GET"
                } else if (method == METHOD_POST) {
                    urlConnection.doOutput = true // write POST params
                    urlConnection.requestMethod = "POST"
                }

                //write POST data 
                if (method == METHOD_POST) {
                    val postData = StringBuilder()
                    for ((key, value) in params) {
                        if (postData.isNotEmpty()) postData.append('&')
                        postData.append(URLEncoder.encode(key, "UTF-8"))
                        postData.append('=')
                        postData.append(URLEncoder.encode(value, "UTF-8"))
                    }
                    val postDataBytes = postData.toString().toByteArray(charset("UTF-8"))
                    urlConnection.setRequestProperty("Content-Length", postDataBytes.size.toString())
                    urlConnection.outputStream.write(postDataBytes)
                }
                // server response code
                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK && callback != null) {
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    // callback success
                    callback.OnSuccess(response.toString())
                    reader.close() // close BufferReader
                } else callback?.OnError(responseCode, urlConnection.responseMessage)
                urlConnection.disconnect() // disconnect connection
            } catch (e: IOException) {
                e.printStackTrace()
                callback?.OnError(500, e.localizedMessage)
            }
        }.start() // start thread
    }




    // static method
    fun reqGET(web_url: String, method: Int, params: HashMap<String, String>, callback: Callback?) {

        // thread for handling async task
        Thread {
            try {
                var url = web_url
                // write GET params,append with url
                if (method == METHOD_GET) {
                    for ((key1, value1) in params) {
                        val key = URLEncoder.encode(key1, "UTF-8")
                        val value = URLEncoder.encode(value1, "UTF-8")
                        url += if (!url.contains("?")) {
                            "?$key=$value"
                        } else {
                            "&$key=$value"
                        }
                    }
                }
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                urlConnection.useCaches = false
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded") // handle url encoded form data
                urlConnection.setRequestProperty("charset", "utf-8")
                if (method == METHOD_GET) {
                    urlConnection.requestMethod = "GET"
                } else if (method == METHOD_POST) {
                    urlConnection.doOutput = true // write POST params
                    urlConnection.requestMethod = "POST"
                }

                //write POST data
                if (method == METHOD_POST) {
                    val postData = StringBuilder()
                    for ((key, value) in params) {
                        if (postData.isNotEmpty()) postData.append('&')
                        postData.append(URLEncoder.encode(key, "UTF-8"))
                        postData.append('=')
                        postData.append(URLEncoder.encode(value, "UTF-8"))
                    }
                    val postDataBytes = postData.toString().toByteArray(charset("UTF-8"))
                    urlConnection.setRequestProperty("Content-Length", postDataBytes.size.toString())
                    urlConnection.outputStream.write(postDataBytes)
                }
                // server response code
                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK && callback != null) {
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    // callback success
                    callback.OnSuccess(response.toString())
                    reader.close() // close BufferReader
                } else callback?.OnError(responseCode, urlConnection.responseMessage)
                urlConnection.disconnect() // disconnect connection
            } catch (e: IOException) {
                e.printStackTrace()
                callback?.OnError(500, e.localizedMessage)
            }
        }.start() // start thread
    }

    // Callback interface
    interface Callback {
        // abstract methods
        fun OnSuccess(response: String?)
        fun OnError(status_code: Int, message: String?)
    }
}
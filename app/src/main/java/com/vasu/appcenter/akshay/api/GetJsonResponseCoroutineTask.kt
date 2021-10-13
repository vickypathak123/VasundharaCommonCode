package com.vasu.appcenter.akshay.api

import android.content.Context
import android.util.Log
import com.example.appcenter.jsonparsing.convertStreamToString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vasu.appcenter.akshay.api.APIBuilder.getAuthorizationToken
import kotlinx.coroutines.*
import java.io.*
import java.net.*
import kotlin.coroutines.CoroutineContext

private val TAG = GetJsonResponseCoroutineTask::class.java.simpleName

/**
 * @author Akshay Harsoda
 * @since 24 Apr 2021
 *
 * this class help you to call your API using HTTP Connection.
 * this will help you to call API in below android LOLLIPOP SDK version
 * this will relapse your Async Task API call in Coroutine Scope
 *
 * @param context your application [Context]
 * @param job your coroutine context [Job]
 * @param fModelClass your response model class object
 * @param fMethodType see this [APIMethodType] for pass your API calling type like '[APIMethodType.GET]' or '[APIMethodType.POST]'
 * @param fListener [APIResponseListener] should be implemented or declared for getting your API response,
 * if successfully get API response it will return in [APIResponseListener.onSuccess],
 * or if any error from get API response it will return in [APIResponseListener.onError]
 */
class GetJsonResponseCoroutineTask<T>(
        private val context: Context,
        private val job: Job,
        private val fModelClass: Class<T>,
        private val fMethodType: APIMethodType,
        private val fListener: APIResponseListener<T>,
) : CoroutineScope {

    private var response = ""

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun cancel() {
        job.cancel()
    }

    /**
     * for execute your API request
     *
     * @param withAuthorization if you need your API call with your authorization key
     * @param apiParams your api param data with key and value, default value is null
     * @param params pass your API related data like base url, interface name etc.
     */
    fun execute(withAuthorization: Boolean = false, apiParams: HashMap<String, String>? = null, vararg params: String) = launch {
        val result = doInBackground(withAuthorization = withAuthorization, apiParams = apiParams, params = params) // runs in background thread without blocking the Main Thread
        onPostExecute(result = result)
    }

    private suspend fun doInBackground(withAuthorization: Boolean, apiParams: HashMap<String, String>? = null, vararg params: String): Boolean = withContext(Dispatchers.IO + job) {
        return@withContext getResponse(withAuthorization = withAuthorization, apiParams = apiParams, params = params)
    }

    private fun onPostExecute(result: Boolean) {
        if (result) {
            fListener.onSuccess(fResponse = getResponseModel(fResponse = response, fModelClass = fModelClass))
        } else {
            fListener.onError(fErrorMessage = response)
        }
    }

    /**
     * for get API response in background thread
     *
     * @param withAuthorization if you need your API call with your authorization key
     * @param apiParams your api param data with key and value, default value is null
     * @param params pass your API related data like base url, interface name etc.
     *
     * @return true if successfully get your API response
     */
    private fun getResponse(withAuthorization: Boolean, apiParams: HashMap<String, String>? = null, vararg params: String): Boolean {

        var reqUrl: String = ""

        if (params.isNotEmpty()) {
            for (param: String in params) {
                reqUrl += param
            }
        }

        val jsonStr = when (fMethodType) {
            APIMethodType.GET -> {
                if (withAuthorization) {
                    makeGetServiceCall(
                            reqUrl = reqUrl,
                            authorization = context.getAuthorizationToken,
                            apiParams = apiParams
                    )
                } else {
                    makeGetServiceCall(
                            reqUrl = reqUrl,
                            apiParams = apiParams
                    )
                }
            }
            APIMethodType.POST -> {
                if (withAuthorization) {
                    makePostServiceCall(
                            reqUrl = reqUrl,
                            authorization = context.getAuthorizationToken,
                            apiParams = apiParams
                    )
                } else {
                    makePostServiceCall(
                            reqUrl = reqUrl,
                            apiParams = apiParams
                    )
                }
            }
        }

        try {
            Log.i(TAG, "Response from url: $jsonStr")
            return if (jsonStr != null) {
                response = jsonStr
                true
            } else {
                response = "Couldn't get json from server."
                Log.e(TAG, response)
                false
            }
        } catch (e: Exception) {
            response = e.toString()
            Log.e(TAG, response)
        }
        return false
    }

    /**
     * for get 'GET API' response
     *
     * @param reqUrl your API base URL
     * @param authorization your API authorization key, default value is null
     * @param apiParams your api param data with key and value, default value is null
     *
     * @return your json response as a string
     */
    private fun makeGetServiceCall(reqUrl: String, authorization: String? = null, apiParams: HashMap<String, String>? = null): String? {
        var response: String? = null
        try {
            var reqUrlFinal = reqUrl

            if (apiParams != null) {
                for ((key1, value1) in apiParams) {
                    val key = URLEncoder.encode(key1, "UTF-8")
                    val value = URLEncoder.encode(value1, "UTF-8")

                    reqUrlFinal += if (!reqUrlFinal.contains("?")) {
                        "?$key=$value"
                    } else {
                        "&$key=$value"
                    }
                }
            }

            val url = URL(reqUrlFinal)
            val conn = url.openConnection() as HttpURLConnection

            conn.useCaches = false
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded") // handle url encoded form data
            conn.setRequestProperty("charset", "utf-8")

            conn.requestMethod = "GET"

            if (authorization != null) {
                conn.setRequestProperty("Authorization", authorization)
            }

            conn.connect()

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                // read the response
                val `in`: InputStream = BufferedInputStream(conn.inputStream)
                response = convertStreamToString(`in`)
            }
        } catch (e: MalformedURLException) {
            Log.e(TAG, "MalformedURLException: " + e.message)
        } catch (e: ProtocolException) {
            Log.e(TAG, "ProtocolException: " + e.message)
        } catch (e: IOException) {
            Log.e(TAG, "IOException: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
        return response
    }

    /**
     * for get 'POST API' response
     *
     * @param reqUrl your API base URL
     * @param authorization your API authorization key, default value is null
     * @param apiParams your api param data with key and value, default value is null
     *
     * @return your json response as a string
     */
    private fun makePostServiceCall(reqUrl: String, authorization: String? = null, apiParams: HashMap<String, String>? = null): String? {
        var response: String? = null
        try {
            val url = URL(reqUrl)
            val conn = url.openConnection() as HttpURLConnection

            conn.useCaches = false
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("charset", "utf-8")

            conn.doOutput = true
            conn.requestMethod = "POST"

            if (authorization != null) {
                conn.setRequestProperty("Authorization", authorization)
            }

            if (apiParams != null) {
                val postData = StringBuilder()
                for ((key, value) in apiParams) {
                    if (postData.isNotEmpty()) postData.append('&')
                    postData.append(URLEncoder.encode(key, "UTF-8"))
                    postData.append('=')
                    postData.append(URLEncoder.encode(value, "UTF-8"))
                }
                val postDataBytes = postData.toString().toByteArray(charset("UTF-8"))
                conn.setRequestProperty("Content-Length", postDataBytes.size.toString())
                conn.outputStream.write(postDataBytes)
            }

            conn.connect()

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                // read the response
                val `in`: InputStream = BufferedInputStream(conn.inputStream)
                response = convertStreamToString(`in`)
            }
        } catch (e: MalformedURLException) {
            Log.e(TAG, "MalformedURLException: " + e.message)
        } catch (e: ProtocolException) {
            Log.e(TAG, "ProtocolException: " + e.message)
        } catch (e: IOException) {
            Log.e(TAG, "IOException: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
        return response
    }

    /**
     * for get response model from json response
     *
     * @param fResponse this parameter content your json response string
     * @param fModelClass this parameter content your response model class
     *
     * @return your response model class object
     */
    private fun getResponseModel(fResponse: String, fModelClass: Class<T>): T {
        val itemType = TypeToken.get(fModelClass).type
        return Gson().fromJson(fResponse, itemType)
    }
}

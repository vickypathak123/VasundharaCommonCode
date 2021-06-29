package com.example.appcenter.jsonparsing

/**
 * JsonParserCallback.kt - Call back for json response.
 * @author:  Jignesh N Patel
 * @date: 04-Feb-2021 10:00 AM
 */
interface JsonParserCallback {
    fun onSuccess(response: String)
    fun onFailure(message: String)
}
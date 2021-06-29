package com.example.appcenter.retrofit

import com.example.appcenter.retrofit.model.ModelAPIError
import com.google.gson.Gson
import retrofit2.Response

/**
 *  - .
 * @author:  Jignesh N Patel
 * @date: 16-Feb-2021 1:43 PM
 */
fun getBodyErrorMessage(response: Response<Any>): String {
    return Gson().fromJson(response.errorBody()!!.charStream(), ModelAPIError::class.java).message.toString()
}
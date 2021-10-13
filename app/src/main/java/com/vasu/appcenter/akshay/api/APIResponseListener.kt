package com.vasu.appcenter.akshay.api

import androidx.annotation.UiThread

interface APIResponseListener<T> {
    @UiThread
    fun onSuccess(fResponse : T)
    @UiThread
    fun onError(fErrorMessage: String?)
}
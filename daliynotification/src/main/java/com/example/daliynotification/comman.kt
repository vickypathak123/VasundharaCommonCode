package com.example.daliynotification

import android.content.Context
import android.util.Log

fun Context.LOGD(message: String, TAG: String = this.javaClass.simpleName) {
    Log.d(TAG,message)
}
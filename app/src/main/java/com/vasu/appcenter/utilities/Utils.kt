package com.vasu.appcenter.utilities

import android.content.Context
import android.util.Base64
import android.util.Log
import com.example.daliynotification.NotificationJob
import com.vasu.appcenter.R
import com.vasu.appcenter.SplashActivity
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


private const val TAG = "Utils"

const val fontPath = "fonts/Nexa Regular.otf"
const val fontPathBold = "fonts/Nexa Bold.otf"



fun Context.dailyNotifications() {
    try {
        NotificationJob.Builder(this)
            .setIntervalInDay(0)
            .setHour(17)
            .setMinute(5)
            .setIncludeToday(true)
            .setIcon(R.mipmap.ic_launcher_round)
            .setTitle(getString(R.string.app_name))
            .setMessage(getString(R.string.app_name))
            .setStartActivity(SplashActivity::class.java)
            .build()
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
}

fun getBaseUrl(encoded: String): String {
    val dataDec: ByteArray = Base64.decode(encoded, Base64.DEFAULT)
    var decodedString = ""
    try {
        decodedString = String(dataDec, Charset.forName("UTF-8"))
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    } finally {
        return decodedString
    }
}

fun Context.getReviewBaseUrl(): String {
    return getBaseUrl(getString(R.string.base_url_review))
}

fun Context.getUpdateBaseUrl(): String {
    return getBaseUrl(getString(R.string.base_url_update))
}

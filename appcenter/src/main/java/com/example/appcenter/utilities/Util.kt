package com.example.appcenter.utilities

import android.app.Activity
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.util.Base64
import com.example.appcenter.R
import com.example.jdrodi.utilities.isValidEmail
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.roundToInt

/**
 * ToDo.. Get Application name from string resource
 */
fun Activity.isLastActivity(): Boolean {
    val mngr = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val taskList = mngr.getRunningTasks(10)
    return taskList[0].numActivities == 1 && taskList[0].topActivity!!.className == this.javaClass.name
}


/**
 * ToDo.. Return true if internet or wi-fi connection is working fine
 * <p>
 * Required permission
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.INTERNET"/>
 *
 * @return true if you have the internet connection, or false if not.
 */
@Suppress("DEPRECATION")
fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                else -> false
            }
        }
    } else {
        try {
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        } catch (e: Exception) {
            Log.e("isNetworkAvailable", e.toString())
        }
    }
    return false
}


fun isValidContextForGlide(context: Context?): Boolean {
    if (context == null) {
        return false
    }
    if (context is Activity && context.isFinishing) {
        return false
    }
    return true
}


fun Context.shareApp(msg: String?) {
    isMoreAppsClick = true
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, msg)
        startActivity(Intent.createChooser(intent, "Share Via"))
    } catch (e: java.lang.Exception) {
        android.util.Log.e("shareApp", "shareApp: $e")
    }
}

fun Context.rateApp(pkgName: String?) {
    isMoreAppsClick = true
    try {
        val marketUri = Uri.parse("market://details?id=$pkgName")
        val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
        startActivity(marketIntent)
    } catch (e: ActivityNotFoundException) {
        val marketUri = Uri.parse("https://play.google.com/store/apps/details?id=$pkgName")
        val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
        startActivity(marketIntent)
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


fun Context.getBaseUrlApps(): String {
    return getBaseUrl(getString(R.string.base_url_apps))
}

fun isSDKBelow21(): Boolean {
    // Check if we're running on Android 5.0 or higher
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
}

fun roundToHalf(d: Double): Double {
    return (d * 2).roundToInt() / 2.0
}

/**
 *  Check if enter phone number is valid or not
 *
 * @return true if valid otherwise false
 */
fun String.isValidPhone(): Boolean {
    val phonePattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"
    val pattern = Pattern.compile(phonePattern)
    val matcher: Matcher = pattern.matcher(this)
    return matcher.matches()
}


/**
 *  Check if enter phone number or email  is valid or not
 *
 * @return true if valid otherwise false
 */

public fun String.isValidContactInformation(): Boolean {
    return when {
        this.contains("@") -> {
            this.isValidEmail()
        }
        this.isValidPhone() -> {
            return true
        }
        else -> {
            return false
        }
    }
}






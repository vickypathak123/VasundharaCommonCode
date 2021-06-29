package com.vasu.appcenter.rateandfeedback.library_feedback

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import java.util.*
import kotlin.math.sqrt

fun Context.getAllDeviceInfo(): String {
    val stringBuilder = StringBuilder("==== SYSTEM-INFO ===\n")
    stringBuilder.append("\n Device: " + getDeviceInfo(Device.DEVICE_SYSTEM_VERSION))
    stringBuilder.append("\n SDK Version: " + getDeviceInfo(Device.DEVICE_VERSION))
    stringBuilder.append("""App Version: ${getAppVersion()}""")
    stringBuilder.append("\n Language: " + getDeviceInfo(Device.DEVICE_LANGUAGE))
    stringBuilder.append("\n TimeZone: " + getDeviceInfo(Device.DEVICE_TIME_ZONE))
    stringBuilder.append("\n Total Memory: " + getDeviceInfo(Device.DEVICE_TOTAL_MEMORY))
    stringBuilder.append("\n Free Memory: " + getDeviceInfo(Device.DEVICE_FREE_MEMORY))
    stringBuilder.append("\n Device Type: " + getDeviceInfo(Device.DEVICE_TYPE))
    stringBuilder.append("\n\n\n\n")
    return stringBuilder.toString()
}

private fun Context.getDeviceInfo(device: Device?): String {
    try {
        when (device) {
            Device.DEVICE_LANGUAGE -> return Locale.getDefault().displayLanguage
            Device.DEVICE_TIME_ZONE -> return TimeZone.getDefault().id //(false, TimeZone.SHORT);
            Device.DEVICE_TOTAL_MEMORY -> {
                return getTotalMemory().toString()
            }
            Device.DEVICE_FREE_MEMORY -> return getFreeMemory().toString()
            Device.DEVICE_SYSTEM_VERSION -> return deviceName
            Device.DEVICE_VERSION -> return "SDK " + Build.VERSION.SDK_INT
            Device.DEVICE_TYPE -> return if (isTablet()) {
                if (getDeviceMoreThan5Inch()) {
                    "Tablet"
                } else "Mobile"
            } else {
                "Mobile"
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

@SuppressLint("NewApi")
private fun Context.getTotalMemory(): Long {
    return try {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        mi.totalMem / 1048576L
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

private fun Context.getFreeMemory(): Long {
    return try {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        mi.availMem / 1048576L
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

private val deviceName: String
    get() {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }

private fun capitalize(s: String?): String {
    if (s == null || s.isEmpty()) {
        return ""
    }
    val first = s[0]
    return if (Character.isUpperCase(first)) {
        s
    } else {
        Character.toUpperCase(first).toString() + s.substring(1)
    }
}

private fun Context.isTablet(): Boolean {
    return resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

private fun Context.getDeviceMoreThan5Inch(): Boolean {
    return try {
        val displayMetrics = resources.displayMetrics
        val yInches = displayMetrics.heightPixels / displayMetrics.ydpi
        val xInches = displayMetrics.widthPixels / displayMetrics.xdpi
        val diagonalInches = sqrt(xInches * xInches + yInches * yInches.toDouble())
        diagonalInches >= 7
    } catch (e: Exception) {
        false
    }
}

private fun Context.getAppVersion(): String {
    val pInfo: PackageInfo?
    var version = " "
    try {
        pInfo = packageManager.getPackageInfo(packageName, 0)
        version = pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return version
}

enum class Device {
    DEVICE_TYPE, DEVICE_VERSION, DEVICE_SYSTEM_VERSION, DEVICE_LANGUAGE, DEVICE_TIME_ZONE, DEVICE_TOTAL_MEMORY, DEVICE_FREE_MEMORY
}
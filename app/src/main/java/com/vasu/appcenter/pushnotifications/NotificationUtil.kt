package com.vasu.appcenter.pushnotifications

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun Activity.openPlayStoreFromNotificationClick() {
    val bundle = intent.extras
    if (bundle != null) {
        if (bundle.containsKey(PACKAGE_NAME)) {
            val appPackageName = bundle.getString(PACKAGE_NAME)
            if (!appPackageName.isNullOrBlank()) {
                val intent = try {
                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
                } catch (e: ActivityNotFoundException) {
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName"))
                }
                startActivity(intent)
            }
        }
    }
}


fun Context.openPlayStoreFromNotificationClick() {

    val pkgName = packageName
    if (!packageName.isNullOrBlank()) {
        val intent = try {
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        } catch (e: ActivityNotFoundException) {
            Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
        }
        startActivity(intent)
    }
}
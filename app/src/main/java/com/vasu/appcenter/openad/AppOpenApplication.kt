package com.vasu.appcenter.openad

import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.vasu.appcenter.AppController

/**
 * The Application class that manages AppOpenManager.
 *
 *
 * def lifecycle_version = "2.2.0"
 * implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
 * implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
 * annotationProcessor "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
 */
class AppOpenApplication : AppController() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) { Log.d(LOG_TAG, "onInitializationComplete.") }
        appOpenManager = AppOpenManager(this)
        appOpenManager!!.fetchAd()
    }

    companion object {
        var appOpenManager: AppOpenManager? = null
    }
}
@file:Suppress("unused")

package com.vasu.appcenter.akshay.adshelper

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.StringRes
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

var isInterstitialAdShow = false

/**
 * Extension method to Get String resource for Context.
 */
internal fun Context.getStringRes(@StringRes id: Int) = resources.getString(id)

/**
 * Extension method to get LayoutInflater
 */
internal inline val Context.inflater: LayoutInflater get() = LayoutInflater.from(this)

/**
 * Show the view  (visibility = View.VISIBLE)
 */
internal inline val View.visible: View
    get() {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
        return this
    }

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
internal inline val View.invisible: View
    get() {
        if (visibility != View.INVISIBLE) {
            visibility = View.INVISIBLE
        }
        return this
    }

/**
 * Remove the view (visibility = View.GONE)
 */
internal inline val View.gone: View
    get() {
        if (visibility != View.GONE) {
            visibility = View.GONE
        }
        return this
    }

/**
 * Extension method for add AdMob Ads test devise id's
 *
 * Find This Log in your logcat for get your devise test id
 * I/Ads: Use RequestConfiguration.Builder.setTestDeviceIds(Arrays.asList("33BE2250B43518CCDA7DE426D04EE231"))
 */
fun setTestDeviceIds() {

    val lTestDeviceIds = listOf(
            AdRequest.DEVICE_ID_EMULATOR,
    )
    val lConfiguration = RequestConfiguration.Builder().setTestDeviceIds(lTestDeviceIds).build()

    MobileAds.setRequestConfiguration(lConfiguration)
}

/**
 * Extension method for add different size of Native Ad
 */
enum class NativeAdsSize {
    Big, Medium, Small, ExitDialog, BlurImageDialog
}

fun Activity.hideStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.let {
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            it.hide(WindowInsets.Type.statusBars())
        }
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}

fun Activity.showStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.show(WindowInsets.Type.statusBars())
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }
}
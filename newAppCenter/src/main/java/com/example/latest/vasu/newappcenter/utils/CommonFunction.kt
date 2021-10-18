@file:Suppress("unused")

package com.example.latest.vasu.newappcenter.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

//<editor-fold desc="For Get All Type of Resources">
/**
 * Extension method to Get String resource for Context.
 */
fun Context.getStringRes(@StringRes id: Int) = resources.getString(id)

fun Context.getStringRes(@StringRes id: Int, vararg formatArgs: String) =
    resources.getString(id, *formatArgs)

/**
 * Extension method to Get Color resource for Context.
 */
fun Context.getColorRes(@ColorRes id: Int) = ContextCompat.getColor(this, id)

/**
 * Extension method to Get Drawable for resource for Context.
 */
fun Context.getDrawableRes(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun Context.sdpToPx(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

fun Context.dpToPx(dp: Int) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(),
    resources.displayMetrics
).roundToInt()
//</editor-fold>

//<editor-fold desc="For StatusBar Entity">
fun Activity.hideStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.let {
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            it.hide(WindowInsets.Type.statusBars())
        }
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }
}

fun Activity.showStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.show(WindowInsets.Type.statusBars())
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
    }
}
//</editor-fold>

//<editor-fold desc="For Share & Rate App">
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
//</editor-fold>
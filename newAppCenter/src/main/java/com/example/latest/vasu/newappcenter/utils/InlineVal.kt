@file:Suppress("unused")

package com.example.latest.vasu.newappcenter.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.graphics.drawable.DrawableCompat
import com.example.latest.vasu.newappcenter.R
import com.example.latest.vasu.newappcenter.themeColor
import kotlin.math.roundToInt

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
inline val Context.isOnline: Boolean
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

//<editor-fold desc="For View Data">
/**
 * Show the view  (visibility = View.VISIBLE)
 */
inline val View.visible: View
    get() {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
        return this
    }

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
inline val View.invisible: View
    get() {
        if (visibility != View.INVISIBLE) {
            visibility = View.INVISIBLE
        }
        return this
    }

/**
 * Remove the view (visibility = View.GONE)
 */
inline val View.gone: View
    get() {
        if (visibility != View.GONE) {
            visibility = View.GONE
        }
        return this
    }

/**
 * Remove the view (View.setEnable(true))
 */
inline val View.enable: View
    get() {
        isEnabled = true
        return this
    }

/**
 * Remove the view (View.setEnable(false))
 */
inline val View.disable: View
    get() {
        isEnabled = false
        return this
    }

/**
 * Extension method to get LayoutInflater
 */
inline val Context.inflater: LayoutInflater get() = LayoutInflater.from(this)
//</editor-fold>

//<editor-fold desc="For get Display Data">
/**
 * Extension method to get theme for Context.
 */
inline val Context.isDarkTheme: Boolean get() = resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

/**
 * Extension method to find a device width in pixels
 */
inline val Context.displayWidth: Int get() = resources.displayMetrics.widthPixels

/**
 * Extension method to find a device height in pixels
 */
inline val Context.displayHeight: Int get() = resources.displayMetrics.heightPixels

/**
 * Extension method to find a device density
 */
inline val Context.displayDensity: Float get() = resources.displayMetrics.density

/**
 * Extension method to find a device density in DPI
 */
inline val Context.displayDensityDpi: Int get() = resources.displayMetrics.densityDpi
//</editor-fold>

//<editor-fold desc="For Text Entity">
inline val String.toEditable: Editable get() = Editable.Factory.getInstance().newEditable(this)

inline val String.removeMultipleSpace: String get() = this.trim().replace("\\s+".toRegex(), " ")

inline val String.getFromHtml: Spanned
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(this)
        }
    }
//</editor-fold>

//<editor-fold desc="For KeyBord">
/**
 * Extension method for Hide Key Bord And Clear Focus
 */
inline val View.hideKeyBord: Unit
    get() {
        this.clearFocus()
        (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(this.windowToken, 0)
    }

/**
 * Extension method for Hide Key Bord And Don't Clear Focus
 */
inline val EditText.hideKeyBordWithOutClearFocus: Unit
    get() {
        this.requestFocus()

        (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(this.windowToken, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.showSoftInputOnFocus = false
        } else {
            this.setTextIsSelectable(true)
        }
    }

/**
 * Extension method for Showing Key Bord
 */
inline val View.showKeyBord: Unit
    get() {
        this.requestFocus()
        (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(this, 0)
    }
//</editor-fold>

//<editor-fold desc="For Default Display Bar Height">
/**
 * Extension method for get StatusBar Height
 */
inline val Activity.statusBarHeight: Int
    get() {
        val resourceId =
            baseContext.resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

/**
 * Extension method for get NavigationBar Height
 */
inline val Activity.navigationBarHeight: Int
    get() {
        val resourceId =
            baseContext.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
//</editor-fold>

inline val isSDKBelow21: Boolean get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP

inline val Double.roundToHalf: Double get() = ((this * 2).roundToInt() / 2.0)

inline val Context.isValidContextForGlide: Boolean
    get() {
        if (this is Activity && this.isFinishing) {
            return false
        }
        return true
    }

inline val Context.shapeCategorySelectedDrawable: Drawable?
    get() {
        val unwrappedDrawable: Drawable? =
            this.getDrawableRes(R.drawable.shape_category_selected)
        if (unwrappedDrawable != null) {
            themeColor?.let {
                val wrappedDrawable: Drawable = DrawableCompat.wrap(unwrappedDrawable)
                DrawableCompat.setTint(wrappedDrawable, it)
                return wrappedDrawable
            }
        }

        return unwrappedDrawable
    }


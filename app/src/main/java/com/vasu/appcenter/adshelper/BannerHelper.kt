package com.vasu.appcenter.adshelper

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.widget.FrameLayout
import com.google.android.gms.ads.*
import com.vasu.appcenter.R
import org.jetbrains.anko.windowManager

private val TAG = "Admob_" + BannerHelper::class.java.simpleName

object BannerHelper {
    private var adView: AdView? = null

    fun Context.loadBanner(fAdContainer: FrameLayout) {
        adView = AdView(this)
        adView!!.adUnitId = this.getString(R.string.admob_bannerad_id)
        adView!!.adSize = adSize(fAdContainer)
        //    adView!!.adSize = AdSize.BANNER
        adView!!.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.i(TAG, "onAdLoaded: Banner")
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)
                Log.e(TAG, "onAdFailedToLoad: Banner, Ad failed to load : ${error.responseInfo}")
            }


        }
        adView!!.loadAd(AdRequest.Builder().build())
        fAdContainer.removeAllViews()
        fAdContainer.addView(adView)
    }

    private fun Context.adSize(fAdContainer: FrameLayout): AdSize {
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val density = outMetrics.density
        var adWidthPixels = fAdContainer.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }
        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
    }

    /**
     * Called when leaving the activity
     */
    fun onPause() {
        if (adView != null)
            adView!!.pause()
    }

    /**
     * Called when returning to the activity
     */
    fun onResume() {
        if (adView != null)
            adView!!.resume()
    }

    /**
     * Called before the activity is destroyed
     */
    fun onDestroy() {
        if (adView != null)
            adView!!.destroy()
    }

/*
    /**
     * TODO:  How to use BannerHelper
     */

        // Step - 1 Add layout in your xml file
                 ...
               <include layout="@layout/layout_ad_view" />
                ...


         // Step - 2  Load Banner Ad in initAds()
                if (AdsManager(mContext).isNeedToShowAds()) {
                     loadBanner(ad_view_container)
                }
*/
}
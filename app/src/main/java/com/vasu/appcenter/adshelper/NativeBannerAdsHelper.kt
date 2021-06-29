package com.vasu.appcenter.adshelper

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.vasu.appcenter.R
import org.jetbrains.anko.layoutInflater

object NativeBannerAdsHelper {
    private val TAG_GOOGLE = NativeBannerAdsHelper::class.java.simpleName
    private var nativeGoogleBannerAd: NativeAd? = null

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     */
    fun Context.loadGoogleNativeBanner(fAdContainer: FrameLayout) {
        Log.i(TAG_GOOGLE, Throwable().stackTrace[0].methodName)
        val builder = AdLoader.Builder(this, getString(R.string.admob_nativead_id))
            .forNativeAd { nativeAd ->

                Log.i(TAG_GOOGLE, "forNativeAd")
                // Assumes that your ad layout is in a file call native_ad_layout.xml
                // in the res/layout folder
                val adView = layoutInflater.inflate(R.layout.layout_google_native_banner_ad, null) as NativeAdView
                // This method sets the text, images and the native ad, etc into the ad
                // view.
                populateNativeAdView(nativeAd, adView)
                // Assumes you have a placeholder FrameLayout in your View layout
                // (with id ad_frame) where the ad is to be placed.
                fAdContainer.removeAllViews()
                fAdContainer.addView(adView)
            }


        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)
                Log.e(TAG_GOOGLE, "onAdFailedToLoad: NativeAd, Ad failed to load : ${error.message}")
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        Log.i(TAG_GOOGLE, Throwable().stackTrace[0].methodName)
        // You must call destroy on old ads when you are done with them,
        // otherwise you will have a memory leak.
        nativeGoogleBannerAd?.destroy()
        nativeGoogleBannerAd = nativeAd
        // Set the media view.
        adView.mediaView = adView.findViewById(R.id.ad_media)

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline and media content are guaranteed to be in every NativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        if (nativeAd.mediaContent != null && adView.mediaView != null) {
            adView.mediaView!!.setMediaContent(nativeAd.mediaContent!!)
        }


        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null && adView.bodyView != null) {
            adView.bodyView!!.visibility = View.GONE
        } else if (adView.bodyView != null) {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null && adView.callToActionView != null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else if (adView.callToActionView != null) {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null && adView.iconView == null) {
            adView.iconView!!.visibility = View.GONE
        } else if (adView.iconView != null) {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }


        if (nativeAd.starRating == null && adView.starRatingView != null) {
            adView.starRatingView!!.visibility = View.INVISIBLE
        } else if (adView.starRatingView != null) {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView!!.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null && adView.advertiserView != null) {
            adView.advertiserView!!.visibility = View.INVISIBLE
        } else if (adView.advertiserView != null) {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView!!.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)

    }

    fun onDestroy() {
        if (nativeGoogleBannerAd != null) {
            nativeGoogleBannerAd!!.destroy()
        }
    }


/*
    /**
     * TODO:  How to use Native Advance Banner Helper
     */

        // Step - 1 Add layout in your xml file
                 ...
               <include layout="@layout/layout_ad_view" />
                ...


         // Step - 2  Load Banner Ad in initAds()
                if (AdsManager(mContext).isNeedToShowAds()) {
                     loadNativeAdvance(ad_view_container)
                }
*/


}
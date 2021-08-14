@file:Suppress("unused")

package com.vasu.appcenter.akshay.adshelper

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.vasu.appcenter.R

/**
 * @author Akshay Harsoda
 * @since 05 Aug 2021
 *
 * NativeAdvancedHelper.kt - Simple object which has load and handle your multiple size Native Advanced AD data
 */
object NativeAdvancedHelper {

    private var mNativeAd: NativeAd? = null
    private val TAG = "Admob_${javaClass.simpleName}"

    val getNativeAd: NativeAd?
        get() {
            return mNativeAd
        }

    /**
     * Call this method when you need to load your Native Advanced AD
     * you need to call this method only once in any activity or fragment
     *
     * this method will load your Native Advanced AD with 3 different size like [NativeAdsSize.Small], [NativeAdsSize.Medium], [NativeAdsSize.Big]
     * for Native Advanced AD Size @see [NativeAdsSize] once
     *
     * @param fContext this is a reference to your activity or fragment context
     * @param fSize it indicate your Ad Size
     * @param fLayout FrameLayout for add NativeAd View
     * @param isNeedLayoutShow [by Default value = true] pass false if you do not need to show AD at a time when it's loaded successfully
     */
    fun loadNativeAdvancedAd(
        @NonNull fContext: Context,
        @NonNull fSize: NativeAdsSize,
        @NonNull fLayout: FrameLayout,
        isNeedLayoutShow: Boolean = true
    ) {
        Log.i(TAG, "loadAd: ")

        if (mNativeAd == null) {

            Log.i(TAG, "loadAd: new live Ad")

            val builder = AdLoader.Builder(fContext, fContext.getStringRes(R.string.admob_nativead_id))

            builder.forNativeAd { unifiedNativeAd ->
                loadAdWithPerfectLayout(fContext = fContext, fSize = fSize, fLayout = fLayout, nativeAd = unifiedNativeAd, isNeedLayoutShow = isNeedLayoutShow)
            }

            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    super.onAdFailedToLoad(error)
                    Log.e(TAG, "onAdFailedToLoad: UnifiedNativeAd, Ad failed to load : ${error.message}")
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.e(TAG, "onAdClicked")
                    mNativeAd = null
                    loadNativeAdvancedAd(fContext = fContext, fSize = fSize, fLayout = fLayout, isNeedLayoutShow = isNeedLayoutShow)

                }
            }).build()

            adLoader.loadAd(AdRequest.Builder().build())
        } else {
            Log.i(TAG, "loadAd: old stored Ad")
            loadAdWithPerfectLayout(fContext = fContext, fSize = fSize, fLayout = fLayout, nativeAd = mNativeAd!!, isNeedLayoutShow = isNeedLayoutShow)
        }
    }

    @SuppressLint("InflateParams")
    private fun loadAdWithPerfectLayout(
        @NonNull fContext: Context,
        @NonNull fSize: NativeAdsSize,
        @NonNull fLayout: FrameLayout,
        @NonNull nativeAd: NativeAd,
        isNeedLayoutShow: Boolean = true
    ) {

        val adView = when (fSize) {

            NativeAdsSize.Big -> {
                fContext.inflater.inflate(R.layout.layout_google_native_ad, null) as NativeAdView
            }

            NativeAdsSize.Medium -> {
                fContext.inflater.inflate(R.layout.layout_google_native_banner_ad, null) as NativeAdView
            }

            NativeAdsSize.Small -> {
                fContext.inflater.inflate(R.layout.layout_google_native_banner_small_ad, null) as NativeAdView
            }
        }

        populateNativeAdView(nativeAd, adView)
        fLayout.removeAllViews()
        fLayout.addView(adView)
        if (isNeedLayoutShow) {
            fLayout.visible
        } else {
            fLayout.gone
        }
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        Log.i(TAG, Throwable().stackTrace[0].methodName)
        // You must call destroy on old ads when you are done with them,
        // otherwise you will have a memory leak.
//        mNativeAd?.destroy()
        mNativeAd = nativeAd
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

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline

        if (nativeAd.mediaContent != null && adView.mediaView != null) {
            adView.mediaView!!.setMediaContent(nativeAd.mediaContent!!)
        } else {
            populateNativeAdView(mNativeAd!!, adView)
        }

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
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

        if (nativeAd.icon == null && adView.iconView != null) {
            adView.iconView!!.visibility = View.GONE
        } else if (adView.iconView != null) {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }

        if (nativeAd.price == null && adView.priceView != null) {
            adView.priceView!!.visibility = View.INVISIBLE
        } else if (adView.priceView != null) {
            adView.priceView!!.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }


        if (nativeAd.store == null && adView.storeView != null) {
            adView.storeView!!.visibility = View.INVISIBLE
        } else if (adView.storeView != null) {
            adView.storeView!!.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }


        // for custom view
        if (adView.priceView != null) {
            adView.priceView!!.visibility = View.GONE
        }
        if (adView.storeView != null) {
            adView.storeView!!.visibility = View.GONE
        }

        if (nativeAd.starRating == null && adView.starRatingView != null) {
            adView.starRatingView!!.visibility = View.INVISIBLE
        } else if (adView.starRatingView != null) {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView!!.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null && adView.advertiserView != null) {
            adView.advertiserView!!.visibility = View.GONE
        } else if (adView.advertiserView != null) {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView!!.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)

    }

    fun destroy() {
        mNativeAd?.destroy()
        mNativeAd = null
    }
}
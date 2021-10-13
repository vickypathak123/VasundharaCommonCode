@file:Suppress("unused")

package com.vasu.appcenter.akshay.adshelper

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.vasu.appcenter.R
import com.vasu.appcenter.akshay.demo.BlurDrawable

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
        isNeedLayoutShow: Boolean = true,
        isAdLoaded: () -> Unit = {}
    ) {
        Log.i(TAG, "loadAd: ")

//        if (mNativeAd == null) {

        Log.i(TAG, "loadAd: new live Ad")

        val builder = AdLoader.Builder(fContext, fContext.getStringRes(R.string.admob_nativead_id))

        builder.forNativeAd { unifiedNativeAd ->
            loadAdWithPerfectLayout(
                fContext = fContext,
                fSize = fSize,
                fLayout = fLayout,
                nativeAd = unifiedNativeAd,
                isNeedLayoutShow = isNeedLayoutShow,
                isAdLoaded = isAdLoaded
            )
        }

        /*val videoOptions = VideoOptions.Builder()
            .setStartMuted(false)
            .build()*/

        /*val adOptions: NativeAdOptions = NativeAdOptions.Builder()
//                .setVideoOptions(videoOptions)
            .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_SQUARE)
            .build()

        builder.withNativeAdOptions(adOptions)*/

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)
                Log.e(TAG, "onAdFailedToLoad: UnifiedNativeAd, Ad failed to load : ${error.message}")
            }

            override fun onAdClicked() {
                super.onAdClicked()
                Log.e(TAG, "onAdClicked")
                mNativeAd = null
                loadNativeAdvancedAd(
                    fContext = fContext,
                    fSize = fSize,
                    fLayout = fLayout,
                    isNeedLayoutShow = isNeedLayoutShow,
                    isAdLoaded = isAdLoaded
                )

            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())
//        } else {
//            Log.i(TAG, "loadAd: old stored Ad")
//            loadAdWithPerfectLayout(
//                fContext = fContext,
//                fSize = fSize,
//                fLayout = fLayout,
//                nativeAd = mNativeAd!!,
//                isNeedLayoutShow = isNeedLayoutShow,
//                isAdLoaded = isAdLoaded
//            )
//        }
    }

    @SuppressLint("InflateParams")
    private fun loadAdWithPerfectLayout(
        @NonNull fContext: Context,
        @NonNull fSize: NativeAdsSize,
        @NonNull fLayout: FrameLayout,
        @NonNull nativeAd: NativeAd,
        isNeedLayoutShow: Boolean = true,
        isAdLoaded: () -> Unit = {}
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
            NativeAdsSize.ExitDialog -> {
                fContext.inflater.inflate(R.layout.native_ad_exit_dialog, null) as NativeAdView
            }
            NativeAdsSize.BlurImageDialog -> {

                if (nativeAd.price != null || nativeAd.store != null) {
                    fContext.inflater.inflate(R.layout.native_ad_for_store, null) as ConstraintLayout
                } else {
//                    fContext.inflater.inflate(R.layout.native_ad_for_store, null) as ConstraintLayout
                    fContext.inflater.inflate(R.layout.native_ad_blur_image_ads, null) as NativeAdView
                }
            }
        }


        when (fSize) {
            NativeAdsSize.BlurImageDialog -> {
//                if (nativeAd.price != null || nativeAd.store != null) {
//                    populateNativeAdView(nativeAd, adView)
//                } else {
                    populateBlurImageDialogNativeAdView(nativeAd, adView.findViewById(R.id.native_ad_view))
//                }
            }
            NativeAdsSize.ExitDialog -> {
                populateExitDialogNativeAdView(nativeAd, adView.findViewById(R.id.native_ad_view))
            }
            else -> {
                populateNativeAdView(nativeAd, adView as NativeAdView)
            }
        }

        fLayout.removeAllViews()
        fLayout.addView(adView)
        if (isNeedLayoutShow) {
            fLayout.visible
            isAdLoaded.invoke()
        } else {
            fLayout.gone
        }
    }

    private fun populateBlurImageDialogNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {

        mNativeAd = nativeAd

        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.imageView = adView.findViewById(R.id.iv_bg_main_image)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)

        (adView.headlineView as TextView).text = nativeAd.headline

        if (nativeAd.mediaContent != null && adView.mediaView != null) {
            nativeAd.mediaContent?.let { mediaContent ->
                adView.mediaView?.setMediaContent(mediaContent)
            }
        } else {
            populateBlurImageDialogNativeAdView(mNativeAd!!, adView)
        }


        if (nativeAd.images.size > 0) {
            if (nativeAd.images[0] != null && adView.imageView != null) {
                (adView.imageView as ImageView).setImageDrawable(nativeAd.images[0].drawable!!)

                val blurView: View = adView.findViewById(R.id.blur_view)
                val blurDrawable: BlurDrawable = BlurDrawable(adView.imageView, 20)
                blurView.background = blurDrawable
            }
        }

        if (nativeAd.body == null && adView.bodyView != null) {
            adView.bodyView?.visibility = View.GONE
        } else if (adView.bodyView != null) {
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.icon == null && adView.iconView != null) {
            adView.iconView?.visibility = View.GONE
        } else if (adView.iconView != null) {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon?.drawable
            )
            adView.iconView?.visibility = View.VISIBLE
        }

        if (nativeAd.starRating == null && adView.starRatingView != null) {
            adView.starRatingView?.visibility = View.GONE
            (adView.findViewById(R.id.txt_rating) as TextView?)?.visibility = View.GONE
        } else if (adView.starRatingView != null) {
            (adView.findViewById(R.id.txt_rating) as TextView?)?.text = nativeAd.starRating!!.toFloat().toString()
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView?.visibility = View.VISIBLE
            (adView.findViewById(R.id.txt_rating) as TextView?)?.visibility = View.VISIBLE
        }

        if (nativeAd.callToAction == null && adView.callToActionView != null) {
            adView.callToActionView?.visibility = View.GONE
        } else if (adView.callToActionView != null) {
            adView.callToActionView?.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.store == null && adView.storeView != null) {
            adView.storeView?.visibility = View.GONE
        } else if (adView.storeView != null) {
            adView.storeView?.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }

        if (nativeAd.price == null && adView.priceView != null) {
            adView.priceView?.visibility = View.GONE
        } else if (adView.priceView != null) {
            adView.priceView?.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.advertiser == null && adView.advertiserView != null) {
            adView.advertiserView?.visibility = View.GONE
        } else if (adView.advertiserView != null) {
            adView.advertiserView?.visibility = View.VISIBLE
            (adView.advertiserView as TextView).text = nativeAd.advertiser
        }

        if (adView.storeView?.visibility == View.GONE && adView.priceView?.visibility == View.GONE) {
            (adView.findViewById(R.id.cl_ad_price_store) as View?)?.visibility = View.GONE
        }

        adView.setNativeAd(nativeAd)

    }

    private fun populateExitDialogNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        Log.i(TAG, Throwable().stackTrace[0].methodName)

        mNativeAd = nativeAd

        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.mediaView = adView.findViewById(R.id.ad_media)

        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)


        (adView.headlineView as TextView).text = nativeAd.headline

        if (nativeAd.mediaContent != null && adView.mediaView != null) {
            adView.mediaView!!.setMediaContent(nativeAd.mediaContent!!)
        } else if (adView.mediaView != null) {
            populateExitDialogNativeAdView(nativeAd, adView)
        }

        adView.mediaView?.setImageScaleType(ImageView.ScaleType.CENTER_CROP)

        if (nativeAd.body == null && adView.bodyView != null) {
            adView.bodyView!!.visibility = View.GONE
        } else if (adView.bodyView != null) {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.icon == null && adView.iconView != null) {
            adView.iconView!!.visibility = View.GONE
        } else if (adView.iconView != null) {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }

        if (nativeAd.callToAction == null && adView.callToActionView != null) {
            adView.callToActionView!!.visibility = View.GONE
        } else if (adView.callToActionView != null) {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        adView.setNativeAd(nativeAd)

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
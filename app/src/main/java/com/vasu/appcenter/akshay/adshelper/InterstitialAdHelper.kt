@file:Suppress("unused")

package com.vasu.appcenter.akshay.adshelper

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import com.example.appcenter.utilities.isOnline
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.vasu.appcenter.R
import com.vasu.appcenter.adshelper.AdsManager

/**
 * @author Akshay Harsoda
 * @since 05 Aug 2021
 *
 * InterstitialAdHelper.kt - Simple object which has load and handle your Interstitial AD data
 */
object InterstitialAdHelper {

    private val TAG = "Admob_${javaClass.simpleName}"

    private var mInterstitialAdMob: InterstitialAd? = null

    private var mIsAdMobAdLoaded = false
    private var mIsAnyAdShow = false

    private var mListener: AdMobAdsListener? = null

    internal fun loadAd(@NonNull fContext: Context, @NonNull fListener: AdMobAdsListener) {

        var lInterstitialAd: InterstitialAd?

        InterstitialAd.load(
            fContext,
            fContext.getStringRes(R.string.admob_interstitialad_id),
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.i(TAG, "onAdLoaded: ")

                    lInterstitialAd = interstitialAd
                    fListener.onInterstitialAdLoaded(interstitialAd = interstitialAd)

                    lInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            Log.i(TAG, "onAdDismissedFullScreenContent: ")
                            isInterstitialAdShow = false
                            fListener.onAdClosed()
                        }

                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                            Log.i(TAG, "onAdShowedFullScreenContent: ")
                            lInterstitialAd = null
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            super.onAdFailedToShowFullScreenContent(adError)
                            Log.i(TAG, "onAdFailedToShowFullScreenContent: ")
                        }

                    }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.i(TAG, "onAdFailedToLoad: Interstitial, Ad failed to load : ${adError.responseInfo}")
                    lInterstitialAd = null
                    fListener.onAdFailed()
                }
            }
        )
    }

    /**
     * Call this method when you need to load your Interstitial AD
     * you need to call this method only once in any activity or fragment
     *
     * @param fContext this is a reference to your activity context
     */
    fun loadInterstitialAd(@NonNull fContext: Context) {

        loadAd(fContext, object : AdMobAdsListener {
            override fun onAdLoaded() {
                mIsAdMobAdLoaded = true
            }

            override fun onInterstitialAdLoaded(interstitialAd: InterstitialAd) {
                super.onInterstitialAdLoaded(interstitialAd)
                mIsAdMobAdLoaded = true
                mInterstitialAdMob = interstitialAd
            }

            override fun onAdFailed() {
                mIsAdMobAdLoaded = false
            }

            override fun onAdClosed() {
                mIsAdMobAdLoaded = false
                mIsAnyAdShow = false
                mInterstitialAdMob?.fullScreenContentCallback = null
                mInterstitialAdMob = null
                mListener?.onAdClosed()
            }

        })
    }

    /**
     * Call this method when you need to show Interstitial AD
     * also this method call our offline native dialog AD [NativeAdvancedAdDialogHelper] when Interstitial Ad fails and give call bake on same way
     *
     * Use of this Method
     * activity.isShowInterstitialAd {[your code which has run after AD show or if AD fails to show]}
     *
     * @param onAdClosed this is a call back of your ad close, it will call also if your ad was not showing to the user
     */
    fun FragmentActivity.isShowInterstitialAd(@NonNull onAdClosed: () -> Unit) {
        mListener = object : AdMobAdsListener {
            override fun onAdClosed() {
                Log.i(TAG, "onAdClosed: ")
                isInterstitialAdShow = false
                mIsAnyAdShow = false
                onAdClosed.invoke()
                loadInterstitialAd(this@isShowInterstitialAd)
            }
        }

        mIsAnyAdShow = if (AdsManager(this).isNeedToShowAds() && !mIsAnyAdShow) {
            if (mIsAdMobAdLoaded && mInterstitialAdMob != null) {
                mInterstitialAdMob?.show(this)
                isInterstitialAdShow = true
                Log.i(TAG, "isShowInterstitialAd: Show Interstitial Ad")
                true
            } else {
                if (NativeAdvancedHelper.getNativeAd != null && this.isOnline && !this.isFinishing) {
                    NativeAdvancedAdDialogHelper.display(this.supportFragmentManager) {
                        mIsAnyAdShow = false
                        mListener?.onAdClosed()
                    }
                    true
                } else {
                    false
                }
            }
        } else {
            false
        }

        if (!mIsAnyAdShow) {
            mListener?.onAdClosed()
        }
    }
}
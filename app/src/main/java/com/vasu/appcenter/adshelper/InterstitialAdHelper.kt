package com.vasu.appcenter.adshelper

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.vasu.appcenter.R

private val TAG = "Admob_" + InterstitialAdHelper::class.java.simpleName

class InterstitialAdHelper {

    fun load(mContext: Activity, adListener: InterstitialAdListener) {


        var mInterstitialAd: InterstitialAd? = null
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(mContext, mContext.getString(R.string.admob_interstitialad_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.i(TAG, "onAdFailedToLoad: Interstitial, Ad failed to load : ${adError.responseInfo}")
                mInterstitialAd = null
                adListener.onAdFailedToLoad()
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.i(TAG, "onAdLoaded: ")
                mInterstitialAd = interstitialAd
                adListener.onAdLoaded(interstitialAd)

                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.i(TAG, "onAdDismissedFullScreenContent")
                        adListener.onAdClosed()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                        Log.i(TAG, "onAdFailedToShowFullScreenContent")
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.i(TAG, "onAdShowedFullScreenContent")
                        mInterstitialAd = null
                    }
                }
            }
        })


    }

    interface InterstitialAdListener {
        fun onAdLoaded(interstitialAd: InterstitialAd)
        fun onAdFailedToLoad()
        fun onAdClosed()
    }

    companion object {
        var instance: InterstitialAdHelper? = null
            get() {
                if (field == null) {
                    synchronized(InterstitialAdHelper::class.java) {
                        if (field == null) {
                            field = InterstitialAdHelper()
                        }
                    }
                }
                return field
            }
            private set
    }


/*
    /**
     * TODO:  How to use InterstitialAdHelper
     */

        // Step - 1 implement in your activity/fragment
                ,InterstitialAdListener


        //  Step - 2 declare global variable
                private InterstitialAd interstitial;

        //  Step - 3 load ad in initAds() method

            if (AdsManager(mContext).isNeedToShowAds()) {
                      interstitial = InterstitialAdHelper.instance!!.load(mContext, this)
                }

        //  Step - 4 check if ad is loaded or not while want to show
        if (AdsManager(mContext).isNeedToShowAds() && interstitial != null && interstitial!!.isLoaded) {
              Log.i(TAG, "Ad is loaded..")
              interstitial!!.show();
        } else {
              // perform your action
        }
*/
}
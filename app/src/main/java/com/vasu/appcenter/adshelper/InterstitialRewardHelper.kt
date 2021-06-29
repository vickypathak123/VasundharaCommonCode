package com.vasu.appcenter.adshelper

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.vasu.appcenter.R


private val TAG = "Admob_" + InterstitialRewardHelper::class.java.simpleName

public class InterstitialRewardHelper(private var fActivity: Activity, var rewardedAdListener: RewardedAdListener) {

    var rewardedInterstitialAd: RewardedInterstitialAd? = null
    var mIsLoading = false


    fun loadRewardedAd() {
        if (!mIsLoading) {
            Log.i(TAG, "onRewardedAd: if")
            mIsLoading = true
            val adRequest = AdRequest.Builder().build()
            // Use the test ad unit ID to load an ad.
            RewardedInterstitialAd.load(fActivity, fActivity.getString(R.string.admob_interstitialad_reward_id), adRequest, object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    Log.i(TAG, "onAdLoaded")
                    mIsLoading = false
                    rewardedInterstitialAd = ad
                    rewardedAdListener.onInterstitialRewardedAdLoaded()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError?) {
                    Log.e(TAG, "onAdFailedToLoad: " + loadAdError!!.message)
                    rewardedAdListener.onInterstitialRewardedAdFailedToLoad()
                }
            })

        } else {
            Log.i(TAG, "onRewardedAd: else")
        }
    }

    fun isLoaded(): Boolean {
        return rewardedInterstitialAd != null
    }


    fun showRewardedVideo() {
        if (isLoaded()) {
            rewardedInterstitialAd!!.setFullScreenContentCallback(object : FullScreenContentCallback() {
                /** Called when the ad failed to show full screen content.  */
                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    Log.i(TAG, "onAdFailedToShowFullScreenContent")
                    rewardedInterstitialAd = null
                }

                /** Called when ad showed the full screen content.  */
                override fun onAdShowedFullScreenContent() {
                    Log.i(TAG, "onAdShowedFullScreenContent")
                }

                /** Called when full screen content is dismissed.  */
                override fun onAdDismissedFullScreenContent() {
                    Log.i(TAG, "onAdDismissedFullScreenContent")
                    rewardedAdListener.onInterstitialRewardedAdClosed()
                }
            })

            rewardedInterstitialAd?.show(fActivity,object : OnUserEarnedRewardListener {
                override fun onUserEarnedReward(p0: RewardItem) {
                    Log.i(TAG, "onUserEarnedReward")
                    rewardedAdListener.onInterstitialUserEarnedReward()
                }
            })
        } else {
            Log.d("TAG", "The rewarded ad wasn't ready yet.")
        }

    }

    interface RewardedAdListener {
        fun onInterstitialRewardedAdLoaded() {}
        fun onInterstitialRewardedAdFailedToLoad() {}
        fun onInterstitialRewardedAdClosed()
        fun onInterstitialUserEarnedReward()
    }
}

package com.vasu.appcenter.adshelper

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.vasu.appcenter.R


private val TAG = "Admob_" + RewardVideoHelper::class.java.simpleName

public class RewardVideoHelper(private var fActivity: Activity, var rewardedAdListener: RewardedAdListener) {

    var mRewardedAd: RewardedAd? = null
    var mIsLoading = false


    fun loadRewardedAd() {
        if (!mIsLoading) {
            Log.i(TAG, "onRewardedAd: if")
            mIsLoading = true
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(fActivity, fActivity.getString(R.string.admob_reward_video_id), adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e(TAG, "onAdFailedToLoad: " + adError.message)
                    mRewardedAd = null
                    rewardedAdListener.onRewardedAdFailedToLoad()
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.i(TAG, "onAdLoaded")
                    mIsLoading = false
                    mRewardedAd = rewardedAd
                    rewardedAdListener.onRewardedAdLoaded()
                }
            })
        } else {
            Log.i(TAG, "onRewardedAd: else")
        }
    }

    fun isLoaded(): Boolean {
        return mRewardedAd != null
    }


    fun showRewardedVideo() {
        if (isLoaded()) {
            mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.i(TAG, "onAdDismissedFullScreenContent")
                    rewardedAdListener.onRewardedAdClosed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    Log.i(TAG, "onAdFailedToShowFullScreenContent")

                }

                override fun onAdShowedFullScreenContent() {
                    Log.i(TAG, "onAdShowedFullScreenContent")
                    // Called when ad is dismissed.
                    // Don't set the ad reference to null to avoid showing the ad a second time.
                    mRewardedAd = null
                }
            }
            mRewardedAd?.show(fActivity, OnUserEarnedRewardListener() {
                fun onUserEarnedReward(rewardItem: RewardItem) {
                    Log.i(TAG, "onUserEarnedReward")
                    rewardedAdListener.onUserEarnedReward()
                }
            })
        } else {
            Log.d("TAG", "The rewarded ad wasn't ready yet.")
        }

    }

    interface RewardedAdListener {
        fun onRewardedAdLoaded() {}
        fun onRewardedAdFailedToLoad() {}
        fun onRewardedAdClosed()
        fun onUserEarnedReward()
    }
}

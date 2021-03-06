package com.vasu.appcenter.akshay.adshelper

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.vasu.appcenter.R
import com.vasu.appcenter.adshelper.AdsManager

/**
 * @author Akshay Harsoda
 * @since 05 Aug 2021
 *
 * RewardVideoHelper.kt - Simple object which has load and handle your multiple size Reward Video AD data
 */
object RewardVideoHelper {

    private val TAG = "Admob_${javaClass.simpleName}"

    private var mRewardedAd: RewardedAd? = null

    private var isUserEarnedReward: Boolean = false

    private val isRewardVideoAdLoaded: Boolean
        get() {
            return mRewardedAd != null
        }

    private var mListener: AdMobAdsListener? = null

    private fun loadRewardVideoAd(@NonNull fContext: Context, @NonNull fListener: AdMobAdsListener) {
        var lRewardedAd: RewardedAd?

        fListener.onStartToLoadRewardVideoAd()

        RewardedAd.load(
            fContext,
            fContext.getStringRes(R.string.admob_reward_video_id),
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.i(TAG, "onAdLoaded: ")

                    lRewardedAd = rewardedAd
                    fListener.onRewardVideoAdLoaded(rewardedAd = rewardedAd)

                    lRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            Log.i(TAG, "onAdDismissedFullScreenContent: ")
                            isInterstitialAdShow = false
                            fListener.onAdClosed()
                        }

                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                            Log.i(TAG, "onAdShowedFullScreenContent: ")
                            lRewardedAd = null
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            super.onAdFailedToShowFullScreenContent(adError)
                            Log.i(TAG, "onAdFailedToShowFullScreenContent: ")
                        }

                    }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.i(TAG, "onAdFailedToLoad: RewardVideo, Ad failed to load : ${adError.responseInfo}")
                    lRewardedAd = null
                    fListener.onAdFailed()
                }
            }
        )
    }

    /**
     * Call this method when you need to load your Reward Video AD
     * you need to call this method only once in any activity or fragment
     *
     * @param fContext this is a reference to your activity or fragment context
     */
    fun loadRewardVideoAd(@NonNull fContext: Context) {

        loadRewardVideoAd(fContext, object : AdMobAdsListener {

            override fun onStartToLoadRewardVideoAd() {
                super.onStartToLoadRewardVideoAd()
                mListener?.onStartToLoadRewardVideoAd()
            }

            override fun onRewardVideoAdLoaded(rewardedAd: RewardedAd) {
                super.onRewardVideoAdLoaded(rewardedAd)
                mRewardedAd = rewardedAd
                mListener?.onAdLoaded()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                mRewardedAd?.fullScreenContentCallback = null
                mRewardedAd = null
                mListener?.onUserEarnedReward(isUserEarnedReward = isUserEarnedReward)
            }
        })
    }

    /**
     * Call this method in your onCreate Method of activity or fragment
     *
     * Use of this Method
     * activity.isShowRewardVideoAd(
     *      onStartToLoadRewardVideoAd = {[show progress when start to load reward video AD]},
     *      onUserEarnedReward = { isUserEarnedReward -> [by Default value = false, it's true when user successfully earned reward]},
     *      onAdLoaded = {[hide progress after successfully load reward video AD]}
     * )
     *
     * @param onStartToLoadRewardVideoAd @see [AdMobAdsListener.onStartToLoadRewardVideoAd]
     * @param onUserEarnedReward @see [AdMobAdsListener.onUserEarnedReward]
     * @param onAdLoaded @see [AdMobAdsListener.onAdLoaded]
     */
    fun FragmentActivity.isShowRewardVideoAd(onStartToLoadRewardVideoAd: () -> Unit, onUserEarnedReward: (isUserEarnedReward: Boolean) -> Unit, onAdLoaded: () -> Unit) {
        isUserEarnedReward = false

        mListener = object : AdMobAdsListener {

            override fun onStartToLoadRewardVideoAd() {
                super.onStartToLoadRewardVideoAd()
                onStartToLoadRewardVideoAd.invoke()
            }

            override fun onUserEarnedReward(isUserEarnedReward: Boolean) {
                super.onUserEarnedReward(isUserEarnedReward)
                isInterstitialAdShow = false
                onUserEarnedReward.invoke(isUserEarnedReward)
                loadRewardVideoAd(this@isShowRewardVideoAd)
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                onAdLoaded.invoke()
            }
        }
    }

    /**
     * Call this method in when you need to show in your activity or fragment
     *
     * Use of this Method
     * activity.showRewardVideoAd()
     */
    fun FragmentActivity.showRewardVideoAd() {
        isUserEarnedReward = false

        if (AdsManager(this).isNeedToShowAds() && isRewardVideoAdLoaded) {
            mRewardedAd?.show(this) {
                isUserEarnedReward = true
            }
            isInterstitialAdShow = true
        }
    }
}
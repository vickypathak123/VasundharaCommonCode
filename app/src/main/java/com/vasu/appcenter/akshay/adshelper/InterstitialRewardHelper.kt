package com.vasu.appcenter.akshay.adshelper

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.vasu.appcenter.R
import com.vasu.appcenter.adshelper.AdsManager

/**
 * @author Akshay Harsoda
 * @since 05 Aug 2021
 *
 * InterstitialRewardHelper.kt - Simple object which has load and handle your Interstitial Reward AD data
 */
object InterstitialRewardHelper {

    private val TAG = "Admob_${javaClass.simpleName}"

    private var mRewardedInterstitialAd: RewardedInterstitialAd? = null

    private var isUserEarnedReward: Boolean = false

    private val isRewardedInterstitialAdLoaded: Boolean
        get() {
            return mRewardedInterstitialAd != null
        }

    private var mListener: AdMobAdsListener? = null

    private fun loadRewardedInterstitialAd(@NonNull fContext: Context, @NonNull fListener: AdMobAdsListener) {
        var lRewardedInterstitialAd: RewardedInterstitialAd?

        fListener.onStartToLoadRewardedInterstitialAd()

        RewardedInterstitialAd.load(
            fContext,
            fContext.getStringRes(R.string.admob_interstitialad_reward_id),
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {

                override fun onAdLoaded(rewardedInterstitialAd: RewardedInterstitialAd) {
                    super.onAdLoaded(rewardedInterstitialAd)

                    Log.i(TAG, "onAdLoaded: ")

                    lRewardedInterstitialAd = rewardedInterstitialAd
                    fListener.onRewardInterstitialAdLoaded(rewardedInterstitialAd = rewardedInterstitialAd)

                    lRewardedInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            Log.i(TAG, "onAdDismissedFullScreenContent: ")
                            isInterstitialAdShow = false
                            fListener.onAdClosed()
                        }

                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                            Log.i(TAG, "onAdShowedFullScreenContent: ")
                            lRewardedInterstitialAd = null
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            super.onAdFailedToShowFullScreenContent(adError)
                            Log.i(TAG, "onAdFailedToShowFullScreenContent: ")
                        }

                    }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.i(TAG, "onAdFailedToLoad: RewardedInterstitial, Ad failed to load : ${adError.responseInfo}")
                    lRewardedInterstitialAd = null
                    fListener.onAdFailed()
                }
            }
        )
    }

    /**
     * Call this method when you need to load your Reward Interstitial AD
     * you need to call this method only once in any activity or fragment
     *
     * @param fContext this is a reference to your activity or fragment context
     */
    fun loadRewardedInterstitialAd(@NonNull fContext: Context) {

        loadRewardedInterstitialAd(fContext, object : AdMobAdsListener {

            override fun onStartToLoadRewardedInterstitialAd() {
                super.onStartToLoadRewardedInterstitialAd()
                mListener?.onStartToLoadRewardedInterstitialAd()
            }

            override fun onRewardInterstitialAdLoaded(rewardedInterstitialAd: RewardedInterstitialAd) {
                super.onRewardInterstitialAdLoaded(rewardedInterstitialAd)
                mRewardedInterstitialAd = rewardedInterstitialAd
                mListener?.onAdLoaded()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                mRewardedInterstitialAd?.fullScreenContentCallback = null
                mRewardedInterstitialAd = null
                mListener?.onUserEarnedReward(isUserEarnedReward = isUserEarnedReward)
            }
        })
    }

    /**
     * Call this method in your onCreate Method of activity or fragment
     *
     * Use of this Method
     * activity.isShowRewardedInterstitialAd(
     *      onStartToLoadRewardedInterstitialAd = {[show progress when start to load Reward Interstitial AD]},
     *      onUserEarnedReward = { isUserEarnedReward -> [by Default value = false, it's true when user successfully earned reward]},
     *      onAdLoaded = {[hide progress after successfully load Reward Interstitial AD]}
     * )
     *
     * @param onStartToLoadRewardedInterstitialAd @see [AdMobAdsListener.onStartToLoadRewardedInterstitialAd]
     * @param onUserEarnedReward @see [AdMobAdsListener.onUserEarnedReward]
     * @param onAdLoaded @see [AdMobAdsListener.onAdLoaded]
     */
    fun FragmentActivity.isShowRewardedInterstitialAd(onStartToLoadRewardedInterstitialAd: () -> Unit, onUserEarnedReward: (isUserEarnedReward: Boolean) -> Unit, onAdLoaded: () -> Unit) {
        isUserEarnedReward = false

        mListener = object : AdMobAdsListener {

            override fun onStartToLoadRewardedInterstitialAd() {
                super.onStartToLoadRewardedInterstitialAd()
                onStartToLoadRewardedInterstitialAd.invoke()
            }

            override fun onUserEarnedReward(isUserEarnedReward: Boolean) {
                super.onUserEarnedReward(isUserEarnedReward)
                isInterstitialAdShow = false
                onUserEarnedReward.invoke(isUserEarnedReward)
                loadRewardedInterstitialAd(this@isShowRewardedInterstitialAd)
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
     * activity.showRewardedInterstitialAd()
     */
    fun FragmentActivity.showRewardedInterstitialAd() {
        isUserEarnedReward = false

        if (AdsManager(this).isNeedToShowAds() && isRewardedInterstitialAdLoaded) {
            mRewardedInterstitialAd?.show(this) {
                isUserEarnedReward = true
            }
            isInterstitialAdShow = true
        }
    }
}
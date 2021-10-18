package com.vasu.appcenter.akshay.activity

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vasu.appcenter.akshay.adshelper.*
import com.vasu.appcenter.akshay.adshelper.InterstitialAdHelper.isShowInterstitialAd
import com.vasu.appcenter.akshay.adshelper.InterstitialRewardHelper.isShowRewardedInterstitialAd
import com.vasu.appcenter.akshay.adshelper.InterstitialRewardHelper.showRewardedInterstitialAd
import com.vasu.appcenter.akshay.adshelper.RewardVideoHelper.isShowRewardVideoAd
import com.vasu.appcenter.akshay.adshelper.RewardVideoHelper.showRewardVideoAd
import com.vasu.appcenter.akshay.base.BaseBindingActivity
import com.vasu.appcenter.akshay.demo.FullScreenExitDialog
import com.vasu.appcenter.akshay.demo.FullScreenNativeAdDialog
import com.vasu.appcenter.databinding.ActivityLoadAdBinding

class LoadAdActivity : BaseBindingActivity<ActivityLoadAdBinding>() {

    private var mFullScreenNativeAdDialog : FullScreenNativeAdDialog? = null

    override fun setBinding(layoutInflater: LayoutInflater): ActivityLoadAdBinding {
        return ActivityLoadAdBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): AppCompatActivity {
        return this@LoadAdActivity
    }

    override fun initAds() {
        super.initAds()

        InterstitialAdHelper.loadInterstitialAd(fContext = mActivity)
        RewardVideoHelper.loadRewardVideoAd(fContext = mActivity)
        InterstitialRewardHelper.loadRewardedInterstitialAd(fContext = mActivity)

        NativeAdvancedHelper.loadNativeAdvancedAd(fContext = mActivity, NativeAdsSize.Big, mBinding.flNativeAdPlaceHolderBig)
        NativeAdvancedHelper.loadNativeAdvancedAd(fContext = mActivity, NativeAdsSize.Medium, mBinding.flNativeAdPlaceHolderMedium)
        NativeAdvancedHelper.loadNativeAdvancedAd(fContext = mActivity, NativeAdsSize.Small, mBinding.flNativeAdPlaceHolderSmall)

        GiftIconHelper.loadGiftAd(fContext = mActivity, fivGiftIcon = mBinding.layoutHeader.layoutGiftAd.giftAdIcon, fivBlastIcon = mBinding.layoutHeader.layoutGiftAd.giftBlastAdIcon)
    }

    override fun initView() {
        super.initView()

//        hideStatusBar()

//        setStatusBarColor(Color.RED, StatusIconColorType.Dark)
//        setStatusBarColor(Color.RED)

        mFullScreenNativeAdDialog = FullScreenNativeAdDialog(mActivity)

        mBinding.showRewardVideoAds.alpha = 0.5f
        mBinding.showRewardVideoAds.isEnabled = false

        mActivity.isShowRewardVideoAd(
            onStartToLoadRewardVideoAd = {
                mBinding.showRewardVideoAds.alpha = 0.5f
                mBinding.showRewardVideoAds.isEnabled = false
            },
            onUserEarnedReward = { isUserEarnedReward ->
                Log.e(TAG, "initView: isUserEarnedReward::$isUserEarnedReward")
                mBinding.showRewardVideoAds.alpha = 0.5f
                mBinding.showRewardVideoAds.isEnabled = false
            },
            onAdLoaded = {
                mBinding.showRewardVideoAds.alpha = 1f
                mBinding.showRewardVideoAds.isEnabled = true
            }
        )

        mBinding.showRewardInterstitialAds.alpha = 0.5f
        mBinding.showRewardInterstitialAds.isEnabled = false

        mActivity.isShowRewardedInterstitialAd(
            onStartToLoadRewardedInterstitialAd = {
                mBinding.showRewardInterstitialAds.alpha = 0.5f
                mBinding.showRewardInterstitialAds.isEnabled = false
            },
            onUserEarnedReward = { isUserEarnedReward ->
                Log.e(TAG, "initView: isUserEarnedReward::$isUserEarnedReward")
                mBinding.showRewardInterstitialAds.alpha = 0.5f
                mBinding.showRewardInterstitialAds.isEnabled = false
            },
            onAdLoaded = {
                mBinding.showRewardInterstitialAds.alpha = 1f
                mBinding.showRewardInterstitialAds.isEnabled = true
            }
        )
    }

    override fun initViewListener() {
        super.initViewListener()

        setClickListener(
            mBinding.showInterstitialAds,
            mBinding.showRewardVideoAds,
            mBinding.showRewardInterstitialAds,
            mBinding.showNativeAdBlurImageAds,
            mBinding.showNativeAdImageAds,
        )
    }

    override fun onClick(v: View) {
        when (v) {
            mBinding.showInterstitialAds -> {
                mActivity.isShowInterstitialAd { }
            }
            mBinding.showRewardVideoAds -> {
                mActivity.showRewardVideoAd()
            }
            mBinding.showRewardInterstitialAds -> {
                mActivity.showRewardedInterstitialAd()
            }
            mBinding.showNativeAdBlurImageAds -> {
//                FullScreenNativeAdDialog(mActivity).show()
                mFullScreenNativeAdDialog?.showFullScreenNativeAdDialog()
            }
            mBinding.showNativeAdImageAds -> {
            }
        }
    }

    var mFullScreenExitDialog: FullScreenExitDialog? = null

    override fun onBackPressed() {
        if (mFullScreenExitDialog == null) {
            mFullScreenExitDialog = FullScreenExitDialog(mActivity)
        }

        mFullScreenExitDialog?.showFullScreenExitDialog {

        }
    }
}
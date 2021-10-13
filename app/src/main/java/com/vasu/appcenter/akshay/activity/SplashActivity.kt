package com.vasu.appcenter.akshay.activity

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.example.appcenter.utilities.isOnline
import com.vasu.appcenter.adshelper.AdsManager
import com.vasu.appcenter.akshay.adshelper.InterstitialAdHelper
import com.vasu.appcenter.akshay.adshelper.InterstitialAdHelper.isShowInterstitialAd
import com.vasu.appcenter.akshay.base.BaseBindingActivity
import com.vasu.appcenter.akshay.forceUpdate.ForceUpdateHelper.checkForceUpdateStatus
import com.vasu.appcenter.databinding.ActivitySplashBinding
import com.vasu.appcenter.inapp.InAppPurchaseHelper
import com.vasu.appcenter.pushnotifications.openPlayStoreFromNotificationClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashActivity : BaseBindingActivity<ActivitySplashBinding>(), InAppPurchaseHelper.OnPurchased {

    private val SPLASH_DELAY = 5000L

    private var mAdsCountDownTimer: AdsCountDownTimer? = null

    override fun setBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): AppCompatActivity {
        return this@SplashActivity
    }

    override fun initView() {
        super.initView()

        openPlayStoreFromNotificationClick()

        mActivity.checkForceUpdateStatus(
            fJob = mJob,
            onUpdateNeeded = { shouldUpdate ->
                if (!shouldUpdate) {
                    initBilling()
                }
//                isShouldUpdate = shouldUpdate
            }
        )

    }

    @UiThread
    private fun initBilling() {
        try {
            InAppPurchaseHelper.instance!!.initBillingClient(mActivity, this)
        } catch (e: Exception) {
            Log.e(TAG, "initBillingClient: " + e.message)
        }
    }

    override fun onClick(v: View) {

    }

    //<editor-fold desc="Check if in-app or subscription already purchased">
    override fun onPurchasedSuccess(purchase: Purchase) {

    }

    override fun onProductAlreadyOwn() {

    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        GlobalScope.launch(Dispatchers.Main) {
            InAppPurchaseHelper.instance!!.initProducts()
            Log.i(TAG, "IN_APP_BILLING | Done")
            redirectToNextActivity()
        }
    }

    override fun onBillingUnavailable() {
        redirectToNextActivity()
    }

    override fun onBillingKeyNotFound(productId: String) {

    }
    //</editor-fold>

    //<editor-fold desc="Call Next Screen">
    private fun redirectToNextActivity() {
        // Check if in-app or subscription already purchased
        if (AdsManager(mActivity).isNeedToShowAds()) {
            startSplashWithAdDelay()
        } else {
            startSplashWithOutAdDelay()
        }
    }

    private fun startSplashWithAdDelay() {
        if (isOnline) {
            mAdsCountDownTimer?.cancel()
            mAdsCountDownTimer = AdsCountDownTimer(SPLASH_DELAY, 1000)
            mAdsCountDownTimer?.start()
            mBinding.tvCountDown.text = (SPLASH_DELAY / 1000).toString()

            InterstitialAdHelper.loadInterstitialAd(mActivity, onAdLoaded = {
                mAdsCountDownTimer?.cancel()
                mActivity.isShowInterstitialAd {
                    startSplashWithOutAdDelay()
                }
            })

        } else {
            startSplashWithOutAdDelay()
        }
    }

    private fun startSplashWithOutAdDelay() {
        launchActivity(
            fIntent = getActivityIntent<CheckAPIActivity>(
//                fNextActivityClass = CheckAPIActivity::class.java
            ),
            isNeedToFinish = true
        )
    }
    //</editor-fold>

    /**
     * AdsCountDownTimer for 5 sec delay
     */
    inner class AdsCountDownTimer(private val millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            mBinding.tvCountDown.text = (((millisInFuture - millisUntilFinished) / 1000) + 1).toString()
        }

        override fun onFinish() {
            Log.i(TAG, "countDownTimer: onFinish")
            mActivity.isShowInterstitialAd {
                startSplashWithOutAdDelay()
            }
        }
    }
}
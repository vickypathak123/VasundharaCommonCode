package com.vasu.appcenter

import android.app.Activity
import android.content.Context
import android.os.*
import android.util.Log
import android.view.View
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.example.appcenter.jsonparsing.JsonParserCallback
import com.example.appcenter.utilities.isOnline
import com.example.appcenter.utilities.isSDKBelow21
import com.example.jdrodi.utilities.OnPositive
import com.example.jdrodi.utilities.showAlert
import com.vasu.appcenter.adshelper.AdsManager
import com.vasu.appcenter.adshelper.EventsHelper.addEvent
import com.vasu.appcenter.inapp.InAppPurchaseHelper
import com.vasu.appcenter.updatejsonparsing.GetJsonUpdateResponseTask
import com.vasu.appcenter.updatejsonparsing.getForceUpdate
import com.vasu.appcenter.updatejsonparsing.saveForceUpdate
import com.vasu.appcenter.openad.AppOpenApplication.Companion.appOpenManager
import com.vasu.appcenter.openad.OnDismissListener
import com.vasu.appcenter.rateandfeedback.ExitSPHelper
import com.vasu.appcenter.rateandfeedback.rateApp
import com.vasu.appcenter.retrofit.APIService
import com.vasu.appcenter.retrofit.model.ForceUpdateModel
import com.vasu.appcenter.utilities.dailyNotifications
import com.vasu.appcenter.utilities.fontPath
import com.vasu.appcenter.utilities.getUpdateBaseUrl
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.exitProcess


private val TAG = OpenAdSplashActivity::class.java.simpleName
private const val SPLASH_DELAY = 8000L

class OpenAdSplashActivity : BaseActivity(), InAppPurchaseHelper.OnPurchased {


    private var isPaused = false // For check in onResume if app is paused or lunch
    private var adsCountDownTimer: AdsCountDownTimer? = null
    //  private var interstitial: InterstitialAd? = null


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(1024, 1024)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        addEvent(TAG)

        // Un-Comment this method to test firebase analytics
        //  forceCrash()
    }


    override fun getContext(): Activity {
        return this@OpenAdSplashActivity
    }

    override fun initViews() {

    }


    override fun initData() {
        //  Call Daily Notification to received offline notification
        dailyNotifications()

        // Need to set save rate dialog dismiss false if it is true
        // So we can again show rate dialog on back press of the main activity
        if (ExitSPHelper(mContext).isDismissed()) {
            ExitSPHelper(mContext).saveDismissed(false)
        }


        if (mContext.isOnline) {
            // Fetch App center data from the server
            if (isSDKBelow21()) {
                // Simple json parsing
                val url = mContext.getUpdateBaseUrl() + "ApkVersion"
                val pkgName = packageName
                val version = BuildConfig.VERSION_NAME.toDouble()
                GetJsonUpdateResponseTask(object : JsonParserCallback {
                    override fun onSuccess(response: String) {
                        Log.i(TAG, response)
                        saveForceUpdate(response)
                        checkUpdateStatus(getForceUpdate()!!)
                    }

                    override fun onFailure(message: String) {
                        Log.e(TAG, message)
                        initBilling()
                    }

                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, pkgName, version.toString())

            } else {
                // Using retrofit with kotlin coroutine
                // To verify force update status
                launch {
                    checkForceUpdateStatus()
                }
            }
        } else {
            Log.i(TAG, "offline")
            initBilling()
        }
    }


    private suspend fun checkForceUpdateStatus() {
        return withContext(Dispatchers.IO) {
            val retroApiInterface = APIService().getUpdateClient(mContext)
            try {
                val pkgName = packageName
                val version = BuildConfig.VERSION_NAME.toDouble()

                val reqUpdateStatus = retroApiInterface.getUpdateStatusAsync(pkgName, version)
                val resUpdateStatus = reqUpdateStatus.await()
                if (resUpdateStatus.isSuccessful && resUpdateStatus.body() != null) {
                    val body = resUpdateStatus.body()!!
                    Log.e(TAG, "message: " + body.message)
                    checkUpdateStatus(body)
                } else {
                    Log.e(TAG, "isSuccessful: false")
                }

            } catch (exception: Exception) {
                Log.e(TAG, exception.toString())
                initBilling()
            }
        }
    }

    private fun checkUpdateStatus(forceUpdateModel: ForceUpdateModel) {
        Log.e(TAG, "message: " + forceUpdateModel.message)
        if (forceUpdateModel.is_need_to_update) {
            Log.i(TAG, "is_need_to_update: true")
            runOnUiThread {
                progress.visibility = View.GONE
                showAlert(
                    getString(R.string.update_required),
                    getString(R.string.update_message),
                    getString(R.string.update_positive),
                    getString(R.string.update_negative),
                    fontPath,
                    object : OnPositive {
                        override fun onYes() {
                            rateApp()
                            finish()
                        }

                        override fun onNo() {
                            super.onNo()
                            finishAffinity()
                        }
                    })
            }
        } else {
            Log.e(TAG, "is_need_to_update: false")
            initBilling()
        }
    }

    override fun initActions() {

    }

    override fun initAds() {

    }

    // Every time to need initialize billing
    // to check if subscription or in-app purchase was made or not and even if it is made then to check is it active or expired
    private fun initBilling() {
        runOnUiThread {
            // interstitial = InterstitialAdHelper.instance!!.load(mContext, this)
            try {
                InAppPurchaseHelper.instance!!.initBillingClient(mContext, this)
            } catch (e: Exception) {
                Log.e(TAG, "initBillingClient: " + e.message)
            }
        }
    }

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }


    /**
     * Called when leaving the mContext
     */
    public override fun onPause() {
        super.onPause()
        if (adsCountDownTimer != null) {
            adsCountDownTimer!!.cancel()
        }
        isPaused = true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.i(TAG, "onBackPressed")
        exitProcess(0)
    }

    /**
     * Called when returning to the mContext
     */
    public override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
        // Perform your task
        Log.i(TAG, "onResume")
        if (isPaused && AdsManager(mContext).isNeedToShowAds() && appOpenManager!!.isAdAvailable) {
            appOpenManager!!.showAdIfAvailable(object : OnDismissListener {
                override fun onDismiss() {
                    startActivity(MainActivity.newIntent(mContext))
                }
            })
        } else if (isPaused) {
            startActivity(MainActivity.newIntent(mContext))
        }


        if (!isPaused) {
            Log.i(TAG, "is not paused")
            return
        }

        Handler().postDelayed({ startHome() }, 100)
        Log.i(TAG, "Ads not loaded or may be null")
    }

    /**
     * AdsCountDownTimer for 5 sec delay
     */
    inner class AdsCountDownTimer(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            tv_count_down.text = (((SPLASH_DELAY - millisUntilFinished) / 1000) + 1).toString()
        }

        override fun onFinish() {
            Log.i(TAG, "countDownTimer: onFinish")
            if (window.decorView.rootView.isShown) {
                startHome()
            } else {
                isPaused = true
            }
        }
    }

    /**
     * If app is not purchased or subscription not found start count down for 5 sec and load ad
     */
    private fun startSplashDelay() {
        adsCountDownTimer = AdsCountDownTimer(SPLASH_DELAY, 1000)
        adsCountDownTimer!!.start()
        tv_count_down.text = (SPLASH_DELAY / 1000).toString()
        Log.i(TAG, "Loading")
    }

    /**
     * Start your splash home our desire activity
     */
    private fun startHome() {
        if (adsCountDownTimer != null) {
            adsCountDownTimer!!.cancel()
        }

        if (AdsManager(mContext).isNeedToShowAds() && appOpenManager!!.isAdAvailable) {
            appOpenManager!!.showAdIfAvailable(object : OnDismissListener {
                override fun onDismiss() {
                    startActivity(MainActivity.newIntent(mContext))
                }
            })
        } else {
            startActivity(MainActivity.newIntent(mContext))
        }
    }

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

    private fun redirectToNextActivity() {
        // Check if in-app or subscription already purchased
        if (AdsManager(mContext).isNeedToShowAds()) {
            startSplashDelay()
        } else {
            startHome()
        }
    }

    private fun forceCrash() {
        throw  RuntimeException("Test Crash") // Force a crash
    }

}
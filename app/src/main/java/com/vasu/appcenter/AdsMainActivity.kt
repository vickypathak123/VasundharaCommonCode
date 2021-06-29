package com.vasu.appcenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.example.jdrodi.BaseActivity
import com.vasu.appcenter.adshelper.AdsManager
import com.vasu.appcenter.inapp.InAppPurchaseHelper
import com.vasu.appcenter.offlineads.OfflineNativeAdDialogHelper
import com.vasu.appcenter.offlineads.OfflineNativeAdvancedHelper
import com.vasu.appcenter.offlineads.OfflineNativeAdvancedHelper.loadOfflineNativeAdvance
import com.vasu.appcenter.rateandfeedback.displayExitDialog
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_ads_main.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_ad_view.*
import kotlinx.android.synthetic.main.layout_gift_icon.*
import org.jetbrains.anko.toast


private val TAG = AdsMainActivity::class.java.simpleName

class AdsMainActivity : BaseActivity(), InAppPurchaseHelper.OnPurchased {


    companion object {
        fun newIntent(mContext: Context): Intent {
            return Intent(mContext, MainActivity::class.java)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads_main)

    }

    override fun getContext(): Activity {
        return this@AdsMainActivity
    }

    override fun initActions() {
        btn_saved_ad.setOnClickListener {
            // startActivity(OfflineAdActivity.newIntent(mContext))

            if (OfflineNativeAdvancedHelper.unNativeAd != null) {
                /*   loadNativeAdvanceDialog {
                       toast("Ads closed")
                   }*/

                OfflineNativeAdDialogHelper.display(supportFragmentManager) {
                    toast("Ads closed")
                }

            } else {
                toast("Ads not loaded")
            }

        }
    }

    override fun initData() {

    }


    override fun onBackPressed() {
        displayExitDialog()
    }


    override fun initAds() {
        if (AdsManager(mContext).isNeedToShowAds()) {
            loadOfflineNativeAdvance(ad_view_container)
        }
    }

    private fun removeAds() {

    }


    override fun onPurchasedSuccess(purchase: Purchase) {
        Log.i(TAG, "purchase")

    }

    override fun onProductAlreadyOwn() {
        Log.i(TAG, "onProductAlreadyOwn")

    }


    override fun onBillingSetupFinished(billingResult: BillingResult) {

    }

    override fun onBillingUnavailable() {

    }

    override fun onBillingKeyNotFound(productId: String) {

    }


}

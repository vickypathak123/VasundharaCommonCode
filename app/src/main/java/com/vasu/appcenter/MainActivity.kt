package com.vasu.appcenter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.example.appcenter.MoreAppsActivity
import com.example.gallery.AGallery
import com.example.gallery.MimeType
import com.example.gallery.engine.impl.GlideEngine
import com.example.gallery.imagecrop.CropImageActivity
import com.example.gallery.imagecrop.ResultActivity
import com.example.gallery.internal.entity.CaptureStrategy
import com.example.jdrodi.BaseActivity
import com.example.jdrodi.utilities.showPermissionsAlert
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.vasu.appcenter.adshelper.AdsManager
import com.vasu.appcenter.adshelper.GiftIconHelper
import com.vasu.appcenter.adshelper.InterstitialRewardHelper
import com.vasu.appcenter.adshelper.NativeBannerAdsHelper.loadGoogleNativeBanner
import com.vasu.appcenter.adshelper.RewardVideoHelper
import com.vasu.appcenter.inapp.InAppPurchaseHelper
import com.vasu.appcenter.inapp.showPurchaseSuccess
import com.vasu.appcenter.rateandfeedback.displayExitDialog
import com.vasu.appcenter.utilities.fontPath
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_ad_view.*
import kotlinx.android.synthetic.main.layout_gift_icon.*
import org.jetbrains.anko.toast


private val TAG = MainActivity::class.java.simpleName
const val REQUEST_CODE_CHOOSE = 101
const val REQUEST_CODE_CROP = 102
const val VOICE_RECOGNITION_REQUEST_CODE = 103
val audioPermission = arrayOf(Manifest.permission.RECORD_AUDIO)
val permission_gallery = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)


class MainActivity : BaseActivity(), InAppPurchaseHelper.OnPurchased {

    private var rewardVideoHelper: RewardVideoHelper? = null
    private var interstitialRewardHelper: InterstitialRewardHelper? = null


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
        setContentView(R.layout.activity_main)

    }

    override fun getContext(): Activity {
        return this@MainActivity
    }

    override fun initActions() {
        main_btn_start.setOnClickListener(this)
        main_btn_gallery.setOnClickListener(this)
        iv_remove_ads.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun onClick(view: View) {
        when (view) {
            main_btn_start -> {


                if (interstitialRewardHelper!!.isLoaded()) {
                    interstitialRewardHelper!!.showRewardedVideo()
                } else {
                    launchAppCenter()
                }

            }
            main_btn_gallery -> {
                if (rewardVideoHelper!!.isLoaded()) {
                    rewardVideoHelper!!.showRewardedVideo()
                } else {
                    checkPermissions()
                }

            }
            iv_remove_ads -> {
              //  showPurchaseAlert(PRODUCT_PURCHASED, false)
                checkAudioPermissions()
            }
        }
    }

    override fun onBackPressed() {
        displayExitDialog()
    }


    override fun initAds() {
        if (AdsManager(mContext).isNeedToShowAds()) {
            tv_remove_ads.visibility = View.GONE
            val animation1: Animation = AnimationUtils.loadAnimation(mContext, R.anim.shake)
            iv_remove_ads.startAnimation(animation1)
            InAppPurchaseHelper.instance!!.initBillingClient(mContext, this)

            GiftIconHelper(mContext)
            loadGoogleNativeBanner(ad_view_container)

            rewardVideoHelper = RewardVideoHelper(this, object : RewardVideoHelper.RewardedAdListener {
                override fun onRewardedAdClosed() {
                    Log.i(TAG, "onRewardedAdClosed")
                    checkPermissions()
                    rewardVideoHelper!!.loadRewardedAd()
                }

                override fun onUserEarnedReward() {
                    Log.i(TAG, "onUserEarnedReward")
                }
            })
            rewardVideoHelper!!.loadRewardedAd()


            interstitialRewardHelper = InterstitialRewardHelper(this, object : InterstitialRewardHelper.RewardedAdListener {
                override fun onInterstitialRewardedAdClosed() {
                    Log.i(TAG, "onInterstitialRewardedAdClosed")
                    launchAppCenter()
                    interstitialRewardHelper!!.loadRewardedAd()
                }

                override fun onInterstitialUserEarnedReward() {
                    Log.i(TAG, "onInterstitialUserEarnedReward")
                }

            })
            interstitialRewardHelper!!.loadRewardedAd()


        } else {
            removeAds()
        }
    }

    private fun removeAds() {
        //  moreAppView.visibility = View.GONE
        main_btn_start.visibility = View.GONE
        color_slider.visibility = View.GONE
        tv_remove_ads.visibility = View.VISIBLE
        iv_remove_ads.visibility = View.GONE
        cl_gift.visibility = View.GONE
        ad_view_container.visibility = View.GONE
    }


    private fun launchAppCenter() {
        val shareMsg = "<your msg> $packageName"
        val themeColor = color_slider.selectedColor
        val textColor = R.color.white

        val moreAppIntent = MoreAppsActivity.launchIntent(this, shareMsg, themeColor, textColor)
        startActivity(moreAppIntent)
    }

    override fun onPurchasedSuccess(purchase: Purchase) {
        Log.i(TAG, "purchase")
        showPurchaseSuccess()
        removeAds()
    }

    override fun onProductAlreadyOwn() {
        Log.i(TAG, "onProductAlreadyOwn")
        showPurchaseSuccess()
        removeAds()
    }


    override fun onBillingSetupFinished(billingResult: BillingResult) {

    }

    override fun onBillingUnavailable() {

    }

    override fun onBillingKeyNotFound(productId: String) {
        val message = "SKU Detail not found for product id: $productId"
        toast(message)
    }

    private fun checkPermissions() {

        Dexter.withContext(mContext)
            .withPermissions(*permission_gallery)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    when {
                        report.areAllPermissionsGranted() -> {
                            choosePicture()
                        }
                        report.isAnyPermissionPermanentlyDenied -> {
                            showPermissionsAlert(fontPath)
                        }
                        else -> {
                            toast("Required Permissions not granted")
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()


    }

    private fun choosePicture() {
        AGallery.from(mContext)
            .choose(MimeType.ofImage())
            .countable(true)
            .showSingleMediaType(true)
            .capture(true) // Camera option enable or disable
            .isNeedToShowAd(AdsManager(mContext).isNeedToShowAds())
            .captureStrategy(CaptureStrategy(false, "$packageName.fileprovider", "temp"))
            .maxSelectable(4) // Maximum image selection limit
            .minSelectable(1) // Minimum image selection limit
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .showPreview(true)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .forResult(REQUEST_CODE_CHOOSE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        when (requestCode) {
            REQUEST_CODE_CHOOSE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val mSelected = AGallery.obtainResult(data)
                        Log.d("Gallery", "mSelected: $mSelected")
                        if (mSelected.isNotEmpty() && mSelected.size >= 0) {
                            val selectedUri = mSelected[0]
                            startActivityForResult(CropImageActivity.createIntent(this, selectedUri), REQUEST_CODE_CROP)
                        } else {
                            toast("You have selected " + mSelected.size + " images")
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        toast(getString(R.string.label_cancel_image_selection))
                    }
                    else -> {
                        toast(getString(R.string.label_failed_image_selection))
                    }
                }
            }

            REQUEST_CODE_CROP -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        startActivity(ResultActivity.createIntent(this, data!!.data))
                    }
                    Activity.RESULT_CANCELED -> {
                        toast(getString(R.string.label_cancel_image_crop))
                    }
                    else -> {
                        toast(getString(R.string.label_failed_image_crop))
                    }
                }
            }

            VOICE_RECOGNITION_REQUEST_CODE -> {
                val matches = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (matches != null && matches.isNotEmpty()) {
                    val number = matches[0].filter { it.isLetterOrDigit() }
                    Log.i("TAG_1", number)
                }
            }

        }
    }
    val ENGLISH = "en"
    val HINDI = "hi"
    val MARATHI = "mr"
    val GUJARATI = "gu"


    fun Activity.startVoiceRecognitionActivity() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
       intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, false)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "$GUJARATI-IN")
        // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN")
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak number")
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE)
    }
    // Record audio
    private fun checkAudioPermissions() {
        Dexter.withContext(mContext).withPermissions(*audioPermission).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                when {
                    report!!.areAllPermissionsGranted() -> {
                        startVoiceRecognitionActivity()
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?, token: PermissionToken?) {
                token!!.continuePermissionRequest()
            }
        }).check()

    }






}

package com.vasu.appcenter.akshay.demo

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import com.example.appcenter.utilities.isOnline
import com.vasu.appcenter.R
import com.vasu.appcenter.akshay.adshelper.NativeAdsSize
import com.vasu.appcenter.akshay.adshelper.NativeAdvancedHelper
import com.vasu.appcenter.akshay.adshelper.visible
import com.vasu.appcenter.databinding.DialogFullScreenNativeAdBinding

class FullScreenNativeAdDialog(
    private val activity: Activity,
) : Dialog(activity, R.style.full_screen_dialog) {

    private var mBinding: DialogFullScreenNativeAdBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        mBinding = DialogFullScreenNativeAdBinding.inflate(LayoutInflater.from(context))
        setContentView(mBinding.root)

        window?.let {

            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

            ///////////   Animation  ////////
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(it.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            lp.gravity = Gravity.BOTTOM
            lp.windowAnimations = R.style.DialogAnimation
            it.attributes = lp

        }

        setCancelable(false)
        setCanceledOnTouchOutside(false)

        setOnDismissListener {
            mBinding.flNativeAdPlaceHolder.removeAllViews()
        }

        mBinding.ivCloseAd.setOnClickListener {
            dismiss()
        }
    }

    fun showFullScreenNativeAdDialog() {
        if (!activity.isFinishing && !isShowing && activity.isOnline) {
            NativeAdvancedHelper.loadNativeAdvancedAd(
                fContext = activity,
                fSize = NativeAdsSize.BlurImageDialog,
                fLayout = mBinding.flNativeAdPlaceHolder,
                isAdLoaded = {
                    mBinding.flNativeAdPlaceHolder.visible
                }
            )
            show()
        }
    }
}
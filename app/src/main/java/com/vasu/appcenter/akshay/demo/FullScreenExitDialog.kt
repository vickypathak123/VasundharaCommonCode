package com.vasu.appcenter.akshay.demo

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import androidx.core.content.ContextCompat
import com.example.appcenter.utilities.isOnline
import com.vasu.appcenter.R
import com.vasu.appcenter.akshay.adshelper.NativeAdsSize
import com.vasu.appcenter.akshay.adshelper.NativeAdvancedHelper
import com.vasu.appcenter.akshay.adshelper.gone
import com.vasu.appcenter.akshay.adshelper.visible
import com.vasu.appcenter.databinding.DialogFullScreeExitBinding

class FullScreenExitDialog(
    private val activity: Activity,
) : Dialog(activity), View.OnClickListener {

    private val mTag = javaClass.simpleName

    private var mBinding: DialogFullScreeExitBinding

    private var onOKClick: (() -> Unit)? = null

    private var isPurchase: Boolean = false

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        mBinding = DialogFullScreeExitBinding.inflate(LayoutInflater.from(context))
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
            it.attributes = lp

        }

        setCancelable(false)
        setCanceledOnTouchOutside(false)

        setOnDismissListener {
            isPurchase = !isPurchase
        }

        setClickListener(
            mBinding.btnExitNo, mBinding.btnExitYes
        )

    }

    private fun setClickListener(vararg fView: View) {
        for (lView in fView) {
            lView.setOnClickListener(this)
        }
    }

    fun showFullScreenExitDialog(onClickOK: () -> Unit) {
        if (!activity.isFinishing && !isShowing) {
            setDefaultView()

            this.onOKClick = {
                dismiss()
                onClickOK.invoke()
            }

            show()
        }
    }

    private fun setDefaultView() {
        mBinding.flNativeAdPlaceHolder.gone
        mBinding.cvExitMain.setBackgroundColor(Color.TRANSPARENT)

        if (!isPurchase) {
            if (activity.isOnline) {
                NativeAdvancedHelper.loadNativeAdvancedAd(
                    fContext = activity,
                    fSize = NativeAdsSize.ExitDialog,
                    fLayout = mBinding.flNativeAdPlaceHolder,
                    isAdLoaded = {
//                        mBinding.cvExitMain.setBackgroundColor(ContextCompat.getColor(activity, R.color.test_color))
//                        mBinding.cvExitMain.setBackgroundColor(Color.WHITE)
                        mBinding.flNativeAdPlaceHolder.visible
                    }
                )
            }
        } else {
            mBinding.flNativeAdPlaceHolder.gone
        }
    }

    override fun onClick(v: View) {
        when (v) {
            mBinding.btnExitNo -> {
                dismiss()
            }

            mBinding.btnExitYes -> {
//                onOKClick?.invoke()
                setDefaultView()
            }
        }
    }
}
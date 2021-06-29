package com.vasu.appcenter.offlineads

import android.app.DialogFragment
import android.app.FragmentManager
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.ads.nativead.NativeAdView
import com.vasu.appcenter.R

class OfflineNativeAdDialogHelper(val clickListener: () -> Unit) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
        if (dialog != null) {

            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog!!.window!!.setLayout(width, height)
            dialog!!.window!!.setWindowAnimations(R.style.AppTheme_Slide)
        }
    }


    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.layout_ad_view_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fAdContainer = view.findViewById<FrameLayout>(R.id.ad_view_container)
        val ivCloseAd = view.findViewById<ImageView>(R.id.iv_close_ad)
        val adView = layoutInflater.inflate(R.layout.layout_google_native_dialog_ad, null) as NativeAdView

        if (OfflineNativeAdvancedHelper.unNativeAd != null) {
            OfflineNativeAdvancedHelper.populateNativeAdView(OfflineNativeAdvancedHelper.unNativeAd!!, adView)
            fAdContainer.removeAllViews()
            fAdContainer.addView(adView)
        }

        ivCloseAd.setOnClickListener {
            dialog!!.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        clickListener()
    }

    override fun onResume() {
        super.onResume()
        // isInterstitialShown = true
    }

    companion object {
        const val TAG = "example_dialog"
        fun display(fragmentManager: FragmentManager?, clickListener: () -> Unit): OfflineNativeAdDialogHelper {
            val exampleDialog = OfflineNativeAdDialogHelper(clickListener)
            exampleDialog.show(fragmentManager!!, TAG)
            return exampleDialog
        }
    }


}
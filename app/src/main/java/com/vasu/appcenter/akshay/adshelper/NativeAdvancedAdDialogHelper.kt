@file:Suppress("unused")

package com.vasu.appcenter.akshay.adshelper

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.vasu.appcenter.R
import com.vasu.appcenter.databinding.LayoutAdViewDialogBinding

class NativeAdvancedAdDialogHelper(@NonNull private val onDialogClose: () -> Unit) : DialogFragment() {

    private val TAG = javaClass.simpleName

    lateinit var mBinding: LayoutAdViewDialogBinding

    companion object {
        fun display(@NonNull fragmentManager: FragmentManager, @NonNull onDialogClose: () -> Unit) {
            isInterstitialAdShow = true
            val nativeAdDialog = NativeAdvancedAdDialogHelper(onDialogClose = onDialogClose)
            nativeAdDialog.isCancelable = false
            nativeAdDialog.show(fragmentManager, "native_ad_dialog")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)

        dialog?.let { dialog ->
            with(dialog) {
                window?.let { window ->
                    with(window) {
                        setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        setWindowAnimations(R.style.AppTheme_Slide)
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, null)
        mBinding = LayoutAdViewDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, null)

        if (this::mBinding.isInitialized) {
            if (NativeAdvancedHelper.getNativeAd != null) {
                Log.i(TAG, "onViewCreated: load dialog ad")

                NativeAdvancedHelper.loadNativeAdvancedAd(
                    fContext = requireActivity(),
                    fSize = NativeAdsSize.Big,
                    fLayout = mBinding.adViewContainer
                )
            }

            mBinding.ivCloseAd.setOnClickListener {
                dialog?.dismiss()
            }

        } else {
            dialog?.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogClose.invoke()
    }

    override fun onResume() {
        super.onResume()
        isInterstitialAdShow = true
    }
}
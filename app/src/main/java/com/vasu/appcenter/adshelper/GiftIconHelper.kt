package com.vasu.appcenter.adshelper

import android.app.Activity
import android.graphics.drawable.AnimationDrawable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.vasu.appcenter.R

class GiftIconHelper(private val mActivity: Activity) : View.OnClickListener {
    private var mivGiftIcon: AppCompatImageView = mActivity.findViewById(R.id.iv_gift)
    private val mivBlastIcon: AppCompatImageView = mActivity.findViewById(R.id.iv_gift_blast)
    private var mInterstitialAd: InterstitialAd? = null

    init {
        mivGiftIcon.visibility = View.GONE
        mivBlastIcon.visibility = View.GONE
        mivGiftIcon.setBackgroundResource(R.drawable.animation_list_filling)
        (mivGiftIcon.background as AnimationDrawable).start()
        loadInterstitialAd()
        initViewListener()
    }

    private fun initViewListener() {
        mivGiftIcon.setOnClickListener(this)
    }

    private fun loadInterstitialAd() {

        InterstitialAdHelper.instance!!.load(mActivity, object : InterstitialAdHelper.InterstitialAdListener {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                mivGiftIcon.visibility = View.VISIBLE
                mivBlastIcon.visibility = View.GONE
            }

            override fun onAdFailedToLoad() {
                mivGiftIcon.visibility = View.GONE
                mivBlastIcon.visibility = View.GONE
                loadInterstitialAd()
            }

            override fun onAdClosed() {
                mivGiftIcon.visibility = View.GONE
                mivBlastIcon.visibility = View.GONE
                loadInterstitialAd()
            }
        })

    }

    override fun onClick(v: View) {
        if (v === mivGiftIcon) {
            mivGiftIcon.visibility = View.GONE
            mivBlastIcon.visibility = View.VISIBLE
            mivBlastIcon.setBackgroundResource(R.drawable.blast_anim)
            (mivBlastIcon.background as AnimationDrawable).start()
            if (!isInterstitialAdLoad()) {
                mivGiftIcon.visibility = View.GONE
                mivBlastIcon.visibility = View.GONE
            }
        }
    }

    private fun isInterstitialAdLoad(): Boolean {
        return if (mInterstitialAd != null) {
            mInterstitialAd!!.show(mActivity)
            true
        } else {
            false
        }
    }


/*
    /**
     * TODO:  How to use GiftIconHelper
     */

        // Step - 1 Add layout in your xml file
                 ...
               <include layout="@layout/layout_gift_icon" />
                ...


         // Step - 2  Load Gift Icon in initAds()
                if (AdsManager(mContext).isNeedToShowAds()) {
                     GiftIconHelper(mContext)
                }
*/

}
package com.vasu.appcenter.offlineads

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vasu.appcenter.R
import com.vasu.appcenter.offlineads.OfflineNativeBannerAdsHelper.loadOfflineGoogleNativeBanner
import kotlinx.android.synthetic.main.layout_ad_view.*

class OfflineAdActivity : AppCompatActivity() {

    companion object {
        fun newIntent(mContext: Context): Intent {
            return Intent(mContext, OfflineAdActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline_ad)

        loadOfflineGoogleNativeBanner(ad_view_container)
    }
}
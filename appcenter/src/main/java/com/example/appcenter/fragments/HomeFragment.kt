package com.example.appcenter.fragments

import android.os.Bundle
import android.os.SystemClock
import com.example.appcenter.R
import com.example.appcenter.adapter.TopThreeAppsAdapter
import com.example.appcenter.adapter.TopsSliderAppsExample
import com.example.appcenter.retrofit.model.Home
import com.example.appcenter.retrofit.model.SubCategory
import com.example.appcenter.themeColor
import com.example.appcenter.utilities.rateApp
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
private const val ARG_HOME_APPS = "arg_home_apps"

class HomeFragment : BaseFragment() {
    private var homeApps: ArrayList<Home>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            homeApps = arguments!!.getParcelableArrayList(ARG_HOME_APPS)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }


    override fun initData() {

        val banners = getBannerList()
        val adapter = TopsSliderAppsExample(mContext, banners)
        home_img_slider.setSliderAdapter(adapter)

        val topAppsAdapter = TopThreeAppsAdapter(mContext, homeApps!!,object : TopThreeAppsAdapter.OnPostExecute {
            override fun iconHeight(height: Int) {

            }
        })

        home_rv_apps.adapter = topAppsAdapter



        home_download.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            context!!.rateApp(banners[home_img_slider.currentPagePosition].app_link)
        }

        themeColor?.let {
            home_iv_ad.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
            iv_download_bg.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }

    override fun initActions() {


    }


    companion object {
        @JvmStatic
        fun newInstance(home: List<Home>) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_HOME_APPS, home as ArrayList<Home>)
                }
            }
    }


    // This method is used for find list of apps from the more apps that contain banner image to add in top image slider
    private fun getBannerList(): ArrayList<SubCategory> {
        val banners: ArrayList<SubCategory> = ArrayList()
        for (mApps in homeApps!!) {
            for (app in mApps.sub_category) {
                val bannerImg = app.banner_image
                if (!bannerImg.isNullOrEmpty()) {
                    banners.add(app)
                }
            }
        }
        return banners
    }
}

package com.example.appcenter.fragments

import android.os.Bundle
import android.os.SystemClock
import com.example.appcenter.R
import com.example.appcenter.adapter.OtherAppsAdapter
import com.example.appcenter.adapter.TopThreeAppsAdapter
import com.example.appcenter.adapter.TopsSliderAppsExample
import com.example.appcenter.retrofit.model.Home
import com.example.appcenter.retrofit.model.SubCategory
import com.example.appcenter.themeColor
import com.example.appcenter.utilities.rateApp
import kotlinx.android.synthetic.main.fragment_more_app.*

// TODO: Rename parameter arguments, choose names that match
private const val ARG_MORE_APPS = "arg_more_apps"

class MoreAppFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var moreApps: ArrayList<SubCategory>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            moreApps = arguments!!.getParcelableArrayList(ARG_MORE_APPS)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_more_app
    }

    override fun initActions() {

    }


    override fun initData() {

        //     more_rv_apps.addItemDecoration(GridSpacingItemDecoration(1, 20, true))

        val banners = getBannerList()

        val adapter = TopsSliderAppsExample(mContext, banners)
        more_img_slider.setSliderAdapter(adapter)

        val topAppsAdapter = TopThreeAppsAdapter(mContext, getTop3Apps(),object : TopThreeAppsAdapter.OnPostExecute {
            override fun iconHeight(height: Int) {
                val otherApps = OtherAppsAdapter(mContext, getOtherApps(),height)
                more_rv_apps.adapter = otherApps
            }
        })
        more_rv_top_apps.adapter = topAppsAdapter




        more_download.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            context!!.rateApp(banners[more_img_slider.currentPagePosition].app_link)
        }

        themeColor?.let {
            more_iv_ad.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
            iv_download_bg.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
        }

    }


    companion object {
        @JvmStatic
        fun newInstance(moreApps: List<SubCategory>) =
            MoreAppFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_MORE_APPS, moreApps as ArrayList<SubCategory>)
                }
            }
    }

    // This method is used for find list of apps from the more apps that contain banner image to add in top image slider
    private fun getBannerList(): ArrayList<SubCategory> {
        val banners: ArrayList<SubCategory> = ArrayList()
        for (app in moreApps!!) {
            val bannerImg = app.banner_image
            if (!bannerImg.isNullOrEmpty()) {
                banners.add(app)
            }
        }
        return banners
    }

    private fun getTop3Apps(): ArrayList<Home> {
        val banners: ArrayList<SubCategory> = ArrayList()
        if (moreApps!!.isNotEmpty()) {
            banners.add(moreApps!![0])
        }
        if (moreApps!!.isNotEmpty() && moreApps!!.size >= 2) {
            banners.add(moreApps!![1])
        }
        if (moreApps!!.isNotEmpty() && moreApps!!.size >= 3) {
            banners.add(moreApps!![2])
        }
        val top3Apps = ArrayList<Home>()
        val home = Home(0, 0, "", 0, "", banners)
        top3Apps.add(home)
        return top3Apps
    }


    private fun getOtherApps(): ArrayList<SubCategory> {
        val otherApps: ArrayList<SubCategory> = ArrayList<SubCategory>()
        if (moreApps!!.isNotEmpty() && moreApps!!.size > 3) {
            otherApps.addAll(moreApps!!.subList(3, moreApps!!.size))
        }
        return otherApps
    }

}

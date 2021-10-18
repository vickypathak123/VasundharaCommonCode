package com.example.latest.vasu.newappcenter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.latest.vasu.newappcenter.adapter.TopThreeAppsAdapter
import com.example.latest.vasu.newappcenter.adapter.TopsSliderAppsExample
import com.example.latest.vasu.newappcenter.base.BaseBindingFragment
import com.example.latest.vasu.newappcenter.databinding.FragmentHomeBinding
import com.example.latest.vasu.newappcenter.model.Home
import com.example.latest.vasu.newappcenter.model.SubCategory
import com.example.latest.vasu.newappcenter.themeColor
import com.example.latest.vasu.newappcenter.utils.rateApp

class HomeFragment: BaseBindingFragment<FragmentHomeBinding>() {

    private val ARG_HOME_APPS = "arg_home_apps"

    private var homeApps: ArrayList<Home> = ArrayList()
    private var bannerList: ArrayList<SubCategory> = ArrayList()

    companion object {
        @JvmStatic
        fun newInstance(home: ArrayList<Home>) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_HOME_APPS, home)
                }
            }
    }

    override fun setBinding(layoutInflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun initView() {
        super.initView()

        //<editor-fold desc="Get List Data From Intent">
        arguments?.let {
            val lHomeApps: ArrayList<Home>?  = it.getParcelableArrayList(ARG_HOME_APPS)

            lHomeApps?.let { fList ->
                homeApps.removeAll(homeApps)
                homeApps.addAll(fList)
            }
        }
        //</editor-fold>

        themeColor?.let {
            mBinding.sliderCardView.homeIvAd.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
            mBinding.sliderCardView.homeDownload.setBackgroundColor(it)
        }

        bannerList.removeAll(bannerList)
        bannerList.addAll(getBannerList())

        mBinding.sliderCardView.homeImgSlider.setSliderAdapter(TopsSliderAppsExample(mContext, bannerList))

        mBinding.homeRvApps.adapter = TopThreeAppsAdapter(mContext, homeApps,object : TopThreeAppsAdapter.OnPostExecute {
            override fun iconHeight(height: Int) {

            }
        })
    }

    override fun initViewListener() {
        super.initViewListener()
        setClickListener(mBinding.sliderCardView.homeDownload)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v) {
            mBinding.sliderCardView.homeDownload -> {
                mContext.rateApp(bannerList[mBinding.sliderCardView.homeImgSlider.currentPagePosition].appLink)
            }
        }
    }

    // This method is used for find list of apps from the more apps that contain banner image to add in top image slider
    private fun getBannerList(): ArrayList<SubCategory> {
        val banners: ArrayList<SubCategory> = ArrayList()
        for (mApps in homeApps) {
            for (app in mApps.subCategory) {
                val bannerImg = app.bannerImage
                if (bannerImg.isNotEmpty()) {
                    banners.add(app)
                }
            }
        }
        return banners
    }

}
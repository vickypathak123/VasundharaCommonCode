package com.example.latest.vasu.newappcenter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.latest.vasu.newappcenter.adapter.OtherAppsAdapter
import com.example.latest.vasu.newappcenter.adapter.TopThreeAppsAdapter
import com.example.latest.vasu.newappcenter.adapter.TopsSliderAppsExample
import com.example.latest.vasu.newappcenter.base.BaseBindingFragment
import com.example.latest.vasu.newappcenter.databinding.FragmentMoreAppBinding
import com.example.latest.vasu.newappcenter.model.Home
import com.example.latest.vasu.newappcenter.model.SubCategory
import com.example.latest.vasu.newappcenter.themeColor
import com.example.latest.vasu.newappcenter.utils.rateApp

class MoreAppFragment : BaseBindingFragment<FragmentMoreAppBinding>() {

    private val ARG_MORE_APPS = "arg_more_apps"

    private var moreApps: ArrayList<SubCategory> = ArrayList()
    private var bannerList: ArrayList<SubCategory> = ArrayList()

    companion object {
        @JvmStatic
        fun newInstance(moreApps: ArrayList<SubCategory>) =
            MoreAppFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_MORE_APPS, moreApps)
                }
            }
    }

    override fun setBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMoreAppBinding {
        return FragmentMoreAppBinding.inflate(layoutInflater, container, false)
    }

    override fun initView() {
        super.initView()

        //<editor-fold desc="Get List Data From Intent">
        arguments?.let {
            val lMoreApps: ArrayList<SubCategory>? = it.getParcelableArrayList(ARG_MORE_APPS)

            lMoreApps?.let { fList ->
                moreApps.removeAll(moreApps)
                moreApps.addAll(fList)
            }
        }
        //</editor-fold>

        themeColor?.let {
            mBinding.sliderCardView.homeIvAd.setColorFilter(
                it,
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            mBinding.sliderCardView.homeDownload.setBackgroundColor(it)
        }

        bannerList.removeAll(bannerList)
        bannerList.addAll(getBannerList())

        mBinding.sliderCardView.homeImgSlider.setSliderAdapter(
            TopsSliderAppsExample(
                mContext,
                bannerList
            )
        )

        val topAppsAdapter = TopThreeAppsAdapter(
            mContext,
            getTop3Apps(),
            object : TopThreeAppsAdapter.OnPostExecute {
                override fun iconHeight(height: Int) {
                    val otherApps = OtherAppsAdapter(mContext, getOtherApps(), height)
                    mBinding.moreRvApps.adapter = otherApps
                }
            })

        mBinding.moreRvTopApps.adapter = topAppsAdapter
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
        for (app in moreApps) {
            val bannerImg = app.bannerImage
            if (bannerImg.isNotEmpty()) {
                banners.add(app)
            }
        }
        return banners
    }

    private fun getTop3Apps(): ArrayList<Home> {
        val banners: ArrayList<SubCategory> = ArrayList()
        if (moreApps.isNotEmpty()) {
            banners.add(moreApps[0])

            if (moreApps.size >= 2) {
                banners.add(moreApps[1])
            }

            if (moreApps.size >= 3) {
                banners.add(moreApps[2])
            }
        }
        val top3Apps = ArrayList<Home>()
        val home = Home("", 0, 0, "", 0,0, banners)
        top3Apps.add(home)
        return top3Apps
    }

    private fun getOtherApps(): ArrayList<SubCategory> {
        val otherApps: ArrayList<SubCategory> = ArrayList<SubCategory>()
        if (moreApps.isNotEmpty() && moreApps.size > 3) {
            otherApps.addAll(moreApps.subList(3, moreApps.size))
        }
        return otherApps
    }
}
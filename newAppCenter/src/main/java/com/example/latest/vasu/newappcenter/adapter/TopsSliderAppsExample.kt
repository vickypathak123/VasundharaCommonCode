package com.example.latest.vasu.newappcenter.adapter

import android.content.Context
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.latest.vasu.newappcenter.R
import com.example.latest.vasu.newappcenter.autoimageslider.SliderViewAdapter
import com.example.latest.vasu.newappcenter.databinding.ListItemTopSliderBinding
import com.example.latest.vasu.newappcenter.model.SubCategory
import com.example.latest.vasu.newappcenter.utils.isSDKBelow21
import com.example.latest.vasu.newappcenter.utils.rateApp

class TopsSliderAppsExample(
        private val context: Context, private val mSliderItems: ArrayList<SubCategory>
) : SliderViewAdapter<TopsSliderAppsExample.SliderAdapterVH>() {

    private val TAG = javaClass.simpleName

    // variable to track event time
    private var mLastClickTime = 0L
    private val mMinDuration = 1500L

    inner class SliderAdapterVH(val fBinding: ListItemTopSliderBinding) : ViewHolder(fBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        return SliderAdapterVH(
                ListItemTopSliderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                )
        )
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {

        with(viewHolder) {
            with(fBinding) {
                with(mSliderItems[position]) {
                    var glideUrl = bannerImage
                    if (isSDKBelow21 && glideUrl.contains("https")) glideUrl =
                            glideUrl.replace("https", "http")

                    Log.i(TAG, "onBindViewHolder: glideUrl::$glideUrl")

                    Glide.with(itemView)
                            .load(glideUrl)
                            .placeholder(R.drawable.thumb_banner)
                            .transform(CenterCrop())
                            .thumbnail(0.15f)
                            .into(ivAutoImageSlider)

                    itemView.setOnClickListener(View.OnClickListener {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                            return@OnClickListener
                        }
                        mLastClickTime = SystemClock.elapsedRealtime()
                        context.rateApp(appLink)
                    })
                }
            }
        }
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }

}
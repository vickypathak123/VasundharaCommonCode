package com.example.appcenter.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.appcenter.R
import com.example.appcenter.adapter.TopsSliderAppsExample.SliderAdapterVH
import com.example.appcenter.autoimageslider.SliderViewAdapter
import com.example.appcenter.retrofit.model.SubCategory
import com.example.appcenter.utilities.Log
import com.example.appcenter.utilities.isSDKBelow21
import com.example.appcenter.utilities.rateApp

class TopsSliderAppsExample(private val context: Context, private val mSliderItems: List<SubCategory>) : SliderViewAdapter<SliderAdapterVH?>() {


    // variable to track event time
    private var mLastClickTime = 0L
    private val mMinDuration = 1500L

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.list_item_top_slider, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) {
        val (_, _, _, _, _, _, _, _, _, _, _, banner_image) = mSliderItems[position]


        var glideUrl = banner_image!!
        if (isSDKBelow21() && glideUrl.contains("https"))
            glideUrl = glideUrl.replace("https", "http")

        Log.i("EROR_GLIDE0", glideUrl)

        Glide.with(viewHolder!!.itemView)
            .load(glideUrl)
            .placeholder(R.drawable.thumb_banner)
            .transform(CenterCrop())
            .thumbnail(0.15f)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                    Log.i("EROR_GLIDE0", e.toString())
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    Log.i("EROR_GLIDE0", "success")
                    return false
                }
            })
            .into(viewHolder.imageViewBackground)



        viewHolder!!.itemView.setOnClickListener(View.OnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                return@OnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            context.rateApp(mSliderItems[position].app_link)
        })
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }

    class SliderAdapterVH(itemView: View) : ViewHolder(itemView) {
        var imageViewBackground: ImageView = itemView.findViewById(R.id.iv_auto_image_slider)
        var imageGifContainer: ImageView = itemView.findViewById(R.id.iv_gif_container)
        var textViewDescription: TextView = itemView.findViewById(R.id.tv_auto_image_slider)
    }


}
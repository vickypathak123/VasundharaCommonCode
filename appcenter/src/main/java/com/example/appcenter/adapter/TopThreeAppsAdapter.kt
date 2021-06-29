package com.example.appcenter.adapter

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.appcenter.R
import com.example.appcenter.retrofit.model.Home
import com.example.appcenter.retrofit.model.SubCategory
import com.example.appcenter.themeColor
import com.example.appcenter.utilities.RoundedRectangle
import com.example.appcenter.utilities.rateApp


open class TopThreeAppsAdapter(private val mContext: Context, private val homeApps: ArrayList<Home>, val onPostExecute: OnPostExecute) :
    RecyclerView.Adapter<TopThreeAppsAdapter.MyViewHolder>() {

    // variable to track event time
    var mLastClickTime: Long = 0
    private val mMinDuration = 1500

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.list_itme_top_three_apps, parent, false)
        );
    }

    override fun getItemCount(): Int {
        return homeApps.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val app = homeApps[position]
        val titleName = app.name
        if (titleName.trim().isNotEmpty()) {
            holder.tvTitle.text = titleName
        } else {
            holder.tvTitle.visibility = View.GONE
            holder.ivTitleBg.visibility = View.GONE
            val param = holder.cardApp1.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0, 17, 0, 0)
            holder.cardApp1.layoutParams = param
        }
        val filtersApps = homeApps[position].sub_category
        if (filtersApps.isNotEmpty()) {
            val app1 = filtersApps[0]
            app1.let {
                Glide.with(holder.itemView)
                    .load(app1.icon)
                    .placeholder(R.drawable.thumb_small)
                    .thumbnail(0.15f)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .into(holder.ivAppThumb1)
                holder.tvAppName1.text = app1.name
            }


            holder.cardApp1.setOnClickListener {
                // Preventing multiple clicks, using threshold of mMinDuration second
                if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                    return@setOnClickListener
                }
                mLastClickTime = SystemClock.elapsedRealtime()
                mContext.rateApp(app1.app_link)
            }
        }

        if (filtersApps.isNotEmpty() && filtersApps.size >= 2) {
            val app2 = filtersApps[1]
            app2.let {
                Glide.with(mContext)
                    .load(app2.icon)
                    .placeholder(R.drawable.thumb_small)
                    .thumbnail(0.15f)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .into(holder.ivAppThumb2)
                holder.tvAppName2.text = app2.name

                holder.cardApp2.setOnClickListener {
                    // Preventing multiple clicks, using threshold of mMinDuration second
                    if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                        return@setOnClickListener
                    }
                    mLastClickTime = SystemClock.elapsedRealtime()
                    mContext.rateApp(app2.app_link)
                }
            }
        }


        if (filtersApps.isNotEmpty() && filtersApps.size >= 3) {
            val app3 = filtersApps[2]
            app3.let {
                Glide.with(mContext)
                    .load(app3.icon)
                    .placeholder(R.drawable.thumb_small)
                    .thumbnail(0.15f)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .into(holder.ivAppThumb3)
                holder.tvAppName3.text = app3.name

                holder.cardApp3.setOnClickListener {
                    // Preventing multiple clicks, using threshold of mMinDuration second
                    if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                        return@setOnClickListener
                    }
                    mLastClickTime = SystemClock.elapsedRealtime()
                    mContext.rateApp(app3.app_link)
                }
            }
        }


        themeColor?.let {
            holder.ivTitleBg.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
            holder.ivAd1.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
            holder.ivAd2.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
            holder.ivAd3.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
            holder.ivAppBg1.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
            holder.ivAppBg2.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
            holder.ivAppBg3.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)

            val roundedRectangle = RoundedRectangle(themeColor!!)
            holder.tvAppDownload1.background = roundedRectangle
            holder.tvAppDownload2.background = roundedRectangle
            holder.tvAppDownload3.background = roundedRectangle
        }

        if (position == 0) {
            holder.ivAppThumb1.post {
                onPostExecute.iconHeight(holder.ivAppThumb1.height)
            }
        }


    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        var ivTitleBg: ImageView = itemView.findViewById(R.id.iv_title_bg)

        var cardApp1: FrameLayout = itemView.findViewById(R.id.card_app1)
        var ivAppThumb1: ImageView = itemView.findViewById(R.id.iv_app_thumb1)

        var ivAppBg1: ImageView = itemView.findViewById(R.id.iv_app_bg_1)
        var ivAd1: ImageView = itemView.findViewById(R.id.iv_ad_1)

        var tvAppName1: TextView = itemView.findViewById(R.id.tv_app_name1)
        var tvAppDownload1: TextView = itemView.findViewById(R.id.tv_app_download1)

        var cardApp2: FrameLayout = itemView.findViewById(R.id.card_app2)
        var ivAppThumb2: ImageView = itemView.findViewById(R.id.iv_app_thumb2)
        var ivAd2: ImageView = itemView.findViewById(R.id.iv_ad_2)
        var ivAppBg2: ImageView = itemView.findViewById(R.id.iv_app_bg_2)
        var tvAppName2: TextView = itemView.findViewById(R.id.tv_app_name2)
        var tvAppDownload2: TextView = itemView.findViewById(R.id.tv_app_download2)

        var cardApp3: FrameLayout = itemView.findViewById(R.id.card_app3)
        var ivAppThumb3: ImageView = itemView.findViewById(R.id.iv_app_thumb3)
        var ivAd3: ImageView = itemView.findViewById(R.id.iv_ad_3)
        var ivAppBg3: ImageView = itemView.findViewById(R.id.iv_app_bg_3)
        var tvAppName3: TextView = itemView.findViewById(R.id.tv_app_name3)
        var tvAppDownload3: TextView = itemView.findViewById(R.id.tv_app_download3)
    }

    // This method is used for find list of apps from the more apps that contain banner image to add in top image slider
    private fun getFilterList(subCategory: List<SubCategory>): ArrayList<SubCategory> {
        val banners: ArrayList<SubCategory> = ArrayList()
        for (app in subCategory) {
            val bannerImg = app.banner_image
            if (bannerImg.isNullOrEmpty()) {
                banners.add(app)
            }
        }
        return banners
    }

    interface OnPostExecute {
        fun iconHeight(height: Int)
    }
}
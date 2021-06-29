package com.example.appcenter.adapter

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chaek.android.RatingBar
import com.example.appcenter.R
import com.example.appcenter.retrofit.model.SubCategory
import com.example.appcenter.themeColor
import com.example.appcenter.utilities.RoundedRectangle
import com.example.appcenter.utilities.rateApp
import com.example.appcenter.utilities.roundToHalf


open class OtherAppsAdapter(private val mContext: Context, private val mApps: ArrayList<SubCategory>, val height: Int) :
    RecyclerView.Adapter<OtherAppsAdapter.MyViewHolder>() {

    // variable to track event time
    var mLastClickTime: Long = 0
    private val mMinDuration = 1500

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_other_apps, parent, false));
    }

    override fun getItemCount(): Int {
        return mApps.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val app = mApps[position]


        holder.ivThumb.layoutParams.width = height
        holder.ivThumb.layoutParams.height = height
        holder.ivThumb.requestFocus()


        app.let {
            Glide.with(holder.itemView)
                .load(app.icon)
                .placeholder(R.drawable.thumb_small)
                .thumbnail(0.15f)
                .into(holder.ivThumb)

            holder.tvAppName.text = app.name
            holder.tvInstalls.text = app.installed_range

            val rating = app.star!!.toFloat() * 2
            val ratingRound = roundToHalf(rating.toDouble()).toFloat()
            holder.mrRatings.setScore(ratingRound)
        }


        holder.itemView.setOnClickListener(View.OnClickListener {
            // Preventing multiple clicks, using threshold of mMinDuration second
            if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                return@OnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            mContext.rateApp(app.app_link)
        })

        holder.mrRatings.setOnClickListener(View.OnClickListener {
            // Preventing multiple clicks, using threshold of mMinDuration second
            if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                return@OnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            mContext.rateApp(app.app_link)
        })

        themeColor?.let {
            holder.listAppsIvAppBg.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
            holder.listAppsIvAd.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
            val roundedRectangle = RoundedRectangle(themeColor!!)
            holder.tvDownload.background = roundedRectangle

        }


    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivThumb: ImageView = itemView.findViewById(R.id.list_apps_iv_thumb)
        var listAppsIvAppBg: ImageView = itemView.findViewById(R.id.list_apps_iv_app_bg)
        var listAppsIvAd: ImageView = itemView.findViewById(R.id.list_apps_iv_ad)
        var tvAppName: TextView = itemView.findViewById(R.id.list_apps_tv_app_name)
        var mrRatings: RatingBar = itemView.findViewById(R.id.list_apps_mr_app_ratings)
        var tvInstalls: TextView = itemView.findViewById(R.id.list_apps_tv_app_installs)
        var tvDownload: TextView = itemView.findViewById(R.id.list_apps_tv_app_download)
    }
}
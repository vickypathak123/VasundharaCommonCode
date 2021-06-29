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
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.chaek.android.RatingBar
import com.example.appcenter.R
import com.example.appcenter.callback.RVClickListener
import com.example.appcenter.retrofit.model.SubCategory
import com.example.appcenter.utilities.Log
import com.example.appcenter.utilities.roundToHalf


open class AppsAdapter(private val mContext: Context, private val mApps: ArrayList<SubCategory>, private val clickListener: RVClickListener) :
    RecyclerView.Adapter<AppsAdapter.MyViewHolder>() {

    // variable to track event time
    var mLastClickTime: Long = 0
    private val mMinDuration = 1500

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_apps, parent, false));
    }

    override fun getItemCount(): Int {
        return mApps.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val app = mApps[position]

        app.let {
            Glide.with(mContext)
                .load(app.icon)
                .placeholder(R.drawable.thumb_small)
                .thumbnail(0.15f)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(holder.ivThumb)

            holder.tvAppName.text = app.name
            holder.tvInstalls.text = app.installed_range
            val rating = app.star!!.toFloat() * 2
            val ratingRound = roundToHalf(rating.toDouble()).toFloat()
            Log.i("RATINGG", rating.toString())
            Log.i("RATINGG_round", ratingRound.toString())
            holder.mrRatings.setScore(ratingRound)
        }

        holder.tvDownload.setOnClickListener(View.OnClickListener {
            // Preventing multiple clicks, using threshold of mMinDuration second
            if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                return@OnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            clickListener.onItemClick(position)
        })
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivThumb: ImageView = itemView.findViewById(R.id.list_apps_iv_thumb)
        var tvAppName: TextView = itemView.findViewById(R.id.list_apps_tv_app_name)
        var mrRatings: RatingBar = itemView.findViewById(R.id.list_apps_mr_app_ratings)
        var tvInstalls: TextView = itemView.findViewById(R.id.list_apps_tv_app_installs)
        var tvDownload: TextView = itemView.findViewById(R.id.list_apps_tv_app_download)
    }
}
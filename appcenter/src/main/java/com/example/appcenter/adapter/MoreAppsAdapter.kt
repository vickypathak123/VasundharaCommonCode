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
import com.example.appcenter.R
import com.example.appcenter.retrofit.model.Data
import com.example.appcenter.utilities.isValidContextForGlide
import com.example.appcenter.utilities.rateApp


open class MoreAppsAdapter(private val mContext: Context, private val mApps: MutableList<Data>) :
    RecyclerView.Adapter<MoreAppsAdapter.MyViewHolder>() {

    // variable to track event time
    var mLastClickTime: Long = 0
    private val mMinDuration = 1500

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.list_item_more_apps, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mApps.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (isValidContextForGlide(mContext)) {
            val app = mApps[position]
            app.let {
                Glide.with(holder.itemView)
                    .load(app.thumb_image)
                    .placeholder(R.drawable.thumb_small)
                    .thumbnail(0.15f)
                  //  .transform(CenterCrop(), RoundedCorners(55))
                    .into(holder.ivThumb)

                holder.tvAppName.text = app.name
                holder.tvAppName.isSelected = true
            }




            holder.itemView.setOnClickListener(View.OnClickListener {
                // Preventing multiple clicks, using threshold of mMinDuration second
                if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                    return@OnClickListener
                }
                mLastClickTime = SystemClock.elapsedRealtime()
                mContext.rateApp(app.package_name)
            })
        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivThumb: ImageView = itemView.findViewById(R.id.list_apps_iv_thumb)
        var tvAppName: TextView = itemView.findViewById(R.id.list_apps_tv_app_name)
    }
}
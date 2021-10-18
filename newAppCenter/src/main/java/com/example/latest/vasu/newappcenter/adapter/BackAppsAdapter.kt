package com.example.latest.vasu.newappcenter.adapter

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.latest.vasu.newappcenter.R
import com.example.latest.vasu.newappcenter.databinding.ListItemBackAppsBinding
import com.example.latest.vasu.newappcenter.model.Data
import com.example.latest.vasu.newappcenter.themeColor
import com.example.latest.vasu.newappcenter.utils.*


open class BackAppsAdapter(private val mContext: Context, private val mApps: MutableList<Data>) :
    RecyclerView.Adapter<BackAppsAdapter.MyViewHolder>() {

    // variable to track event time
    var mLastClickTime: Long = 0
    private val mMinDuration = 1500

    inner class MyViewHolder(val fBinding: ListItemBackAppsBinding) : RecyclerView.ViewHolder(fBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ListItemBackAppsBinding.inflate(
                LayoutInflater.from(mContext),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mApps.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (mContext.isValidContextForGlide) {

            with(holder) {
                with(fBinding) {
                    with(mApps[position]) {
                        Glide.with(itemView)
                            .load(thumbImage)
                            .placeholder(R.drawable.thumb_small)
                            .thumbnail(0.15f)
                            .into(listAppsIvThumb)

                        itemView.setOnClickListener {
                            // Preventing multiple clicks, using threshold of mMinDuration second
                            if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                                return@setOnClickListener
                            }
                            mLastClickTime = SystemClock.elapsedRealtime()

                            mContext.rateApp(packageName)
                        }

                        themeColor?.let {
                            listAppsIvAd.setColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)
                            listAppsTvAppName.background = mContext.shapeCategorySelectedDrawable
                        }
                    }
                }
            }
        }
    }


}
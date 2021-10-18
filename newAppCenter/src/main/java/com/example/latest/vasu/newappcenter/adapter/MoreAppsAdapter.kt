package com.example.latest.vasu.newappcenter.adapter

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.latest.vasu.newappcenter.R
import com.example.latest.vasu.newappcenter.databinding.ListItemMoreAppsBinding
import com.example.latest.vasu.newappcenter.model.Data
import com.example.latest.vasu.newappcenter.utils.isValidContextForGlide
import com.example.latest.vasu.newappcenter.utils.rateApp


open class MoreAppsAdapter(private val mContext: Context, private val mApps: ArrayList<Data>) :
    RecyclerView.Adapter<MoreAppsAdapter.MyViewHolder>() {

    // variable to track event time
    var mLastClickTime: Long = 0
    private val mMinDuration = 1500

    inner class MyViewHolder(val fBinding: ListItemMoreAppsBinding) :
        RecyclerView.ViewHolder(fBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ListItemMoreAppsBinding.inflate(
                LayoutInflater.from(parent.context),
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

                        listAppsTvAppName.text = name
                        listAppsTvAppName.isSelected = true

                        itemView.setOnClickListener(View.OnClickListener {
                            // Preventing multiple clicks, using threshold of mMinDuration second
                            if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                                return@OnClickListener
                            }
                            mLastClickTime = SystemClock.elapsedRealtime()
                            mContext.rateApp(packageName)
                        })
                    }
                }
            }
        }
    }
}
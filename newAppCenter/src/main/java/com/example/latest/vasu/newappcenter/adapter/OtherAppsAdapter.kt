package com.example.latest.vasu.newappcenter.adapter

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.latest.vasu.newappcenter.R
import com.example.latest.vasu.newappcenter.databinding.ListItemOtherAppsBinding
import com.example.latest.vasu.newappcenter.model.SubCategory
import com.example.latest.vasu.newappcenter.themeColor
import com.example.latest.vasu.newappcenter.utils.rateApp
import com.example.latest.vasu.newappcenter.utils.roundToHalf
import com.example.latest.vasu.newappcenter.utils.shapeCategorySelectedDrawable


open class OtherAppsAdapter(
    private val mContext: Context,
    private val mApps: ArrayList<SubCategory>,
    val height: Int
) :
    RecyclerView.Adapter<OtherAppsAdapter.MyViewHolder>() {

    // variable to track event time
    var mLastClickTime: Long = 0
    private val mMinDuration = 1500

    inner class MyViewHolder(val fBinding: ListItemOtherAppsBinding) : RecyclerView.ViewHolder(fBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ListItemOtherAppsBinding.inflate(
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

        with(holder) {
            with(fBinding) {
                with(mApps[position]) {
                    listAppsIvThumb.layoutParams.width = height
                    listAppsIvThumb.layoutParams.height = height
                    listAppsIvThumb.requestFocus()

                    Glide.with(itemView)
                        .load(icon)
                        .placeholder(R.drawable.thumb_small)
                        .thumbnail(0.15f)
                        .into(listAppsIvThumb)

                    listAppsTvAppName.text = name
                    listAppsTvAppInstalls.text = installedRange

                    val rating = star.toFloat() * 2
                    val ratingRound = rating.toDouble().roundToHalf.toFloat()
                    listAppsMrAppRatings.setScore(ratingRound)

                    itemView.setOnClickListener(View.OnClickListener {
                        // Preventing multiple clicks, using threshold of mMinDuration second
                        if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                            return@OnClickListener
                        }
                        mLastClickTime = SystemClock.elapsedRealtime()
                        mContext.rateApp(appLink)
                    })

                    listAppsMrAppRatings.setOnClickListener(View.OnClickListener {
                        // Preventing multiple clicks, using threshold of mMinDuration second
                        if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                            return@OnClickListener
                        }
                        mLastClickTime = SystemClock.elapsedRealtime()
                        mContext.rateApp(appLink)
                    })

                    themeColor?.let {
                        listAppsIvAppBg.setColorFilter(
                            it,
                            android.graphics.PorterDuff.Mode.SRC_IN
                        )
                        listAppsIvAd.setColorFilter(
                            it,
                            android.graphics.PorterDuff.Mode.SRC_IN
                        )
//                        val roundedRectangle = RoundedRectangle(themeColor!!)
                        listAppsTvAppDownload.background = mContext.shapeCategorySelectedDrawable
                    }
                }
            }
        }
    }
}
package com.example.latest.vasu.newappcenter.adapter

import android.content.Context
import android.graphics.PorterDuffColorFilter
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.latest.vasu.newappcenter.R
import com.example.latest.vasu.newappcenter.databinding.ListItmeTopThreeAppsBinding
import com.example.latest.vasu.newappcenter.model.Home
import com.example.latest.vasu.newappcenter.themeColor
import com.example.latest.vasu.newappcenter.utils.gone
import com.example.latest.vasu.newappcenter.utils.rateApp
import com.example.latest.vasu.newappcenter.utils.shapeCategorySelectedDrawable


open class TopThreeAppsAdapter(
        private val mContext: Context, private val homeApps: ArrayList<Home>, val onPostExecute: OnPostExecute
) : RecyclerView.Adapter<TopThreeAppsAdapter.MyViewHolder>() {

    // variable to track event time
    var mLastClickTime: Long = 0
    private val mMinDuration = 1500

    inner class MyViewHolder(val fBinding: ListItmeTopThreeAppsBinding) : RecyclerView.ViewHolder(fBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
                ListItmeTopThreeAppsBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                )
        )
    }

    override fun getItemCount(): Int {
        return homeApps.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        with(holder) {
            with(fBinding) {
                with(homeApps[position]) {
                    if (name.trim().isNotEmpty()) {
                        tvTitle.text = name
                    } else {
                        tvTitle.gone
                        ivTitleBg.gone
                        val param = cardApp1.layoutParams as ViewGroup.MarginLayoutParams
                        param.setMargins(0, 17, 0, 0)
                        cardApp1.layoutParams = param
                    }

                    if (subCategory.isNotEmpty()) {
                        //<editor-fold desc="For 1st App Data">
                        val app1 = subCategory[0]
                        app1.let {
                            Glide.with(itemView)
                                    .load(app1.icon)
                                    .placeholder(R.drawable.thumb_small)
                                    .thumbnail(0.15f)
                                    .transform(CenterCrop(), RoundedCorners(10))
                                    .into(ivAppThumb1)
                            tvAppName1.text = app1.name
                        }

                        cardApp1.setOnClickListener {
                            // Preventing multiple clicks, using threshold of mMinDuration second
                            if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                                return@setOnClickListener
                            }
                            mLastClickTime = SystemClock.elapsedRealtime()
                            mContext.rateApp(app1.appLink)
                        }
                        //</editor-fold>

                        //<editor-fold desc="For 2nd App Data">
                        if (subCategory.size >= 2) {
                            val app2 = subCategory[1]
                            app2.let {
                                Glide.with(mContext)
                                        .load(app2.icon)
                                        .placeholder(R.drawable.thumb_small)
                                        .thumbnail(0.15f)
                                        .transform(CenterCrop(), RoundedCorners(10))
                                        .into(ivAppThumb2)
                                tvAppName2.text = app2.name

                                cardApp2.setOnClickListener {
                                    // Preventing multiple clicks, using threshold of mMinDuration second
                                    if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                                        return@setOnClickListener
                                    }
                                    mLastClickTime = SystemClock.elapsedRealtime()
                                    mContext.rateApp(app2.appLink)
                                }
                            }
                        }
                        //</editor-fold>

                        //<editor-fold desc="For 3rd App Data">
                        if (subCategory.size >= 3) {
                            val app3 = subCategory[2]
                            app3.let {
                                Glide.with(mContext)
                                        .load(app3.icon)
                                        .placeholder(R.drawable.thumb_small)
                                        .thumbnail(0.15f)
                                        .transform(CenterCrop(), RoundedCorners(10))
                                        .into(ivAppThumb3)
                                tvAppName3.text = app3.name

                                cardApp3.setOnClickListener {
                                    // Preventing multiple clicks, using threshold of mMinDuration second
                                    if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {
                                        return@setOnClickListener
                                    }
                                    mLastClickTime = SystemClock.elapsedRealtime()
                                    mContext.rateApp(app3.appLink)
                                }
                            }
                        }
                        //</editor-fold>
                    }

                    themeColor?.let {

                        val colorFilter = PorterDuffColorFilter(it, android.graphics.PorterDuff.Mode.SRC_IN)

                        ivTitleBg.colorFilter = colorFilter
                        ivAd1.colorFilter = colorFilter
                        ivAd2.colorFilter = colorFilter
                        ivAd3.colorFilter = colorFilter
                        ivAppBg1.colorFilter = colorFilter
                        ivAppBg2.colorFilter = colorFilter
                        ivAppBg3.colorFilter = colorFilter

                        tvAppDownload1.background = mContext.shapeCategorySelectedDrawable
                        tvAppDownload2.background = mContext.shapeCategorySelectedDrawable
                        tvAppDownload3.background = mContext.shapeCategorySelectedDrawable
                    }

                    if (position == 0) {
                        ivAppThumb1.post {
                            onPostExecute.iconHeight(ivAppThumb1.height)
                        }
                    }
                }
            }
        }
    }

    interface OnPostExecute {
        fun iconHeight(height: Int)
    }
}
package com.vasu.appcenter.rateandfeedback

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.hsalf.smilerating.SmileRating
import com.vasu.appcenter.R
import com.vasu.appcenter.adshelper.AdsManager
import com.vasu.appcenter.adshelper.NativeAdvancedHelper.loadNativeAdvance
import com.vasu.appcenter.utilities.fontPath
import com.vasu.appcenter.utilities.fontPathBold

private val TAG = "ExitDialogs"

var RATING = -1


fun Context.exitDialog() {
    try {
        val inflater = (this as Activity).layoutInflater
        val alertLayout = inflater.inflate(R.layout.layout_dialog_exit, null)
        val tvTitle = alertLayout.findViewById<TextView>(R.id.exit_tv_title)
        val tvDesc = alertLayout.findViewById<TextView>(R.id.exit_tv_desc)
        val btnNo = alertLayout.findViewById<TextView>(R.id.exit_btn_no)
        val btnYes = alertLayout.findViewById<TextView>(R.id.exit_btn_yes)
        val tf = Typeface.createFromAsset(assets, fontPath)
        val tfBold = Typeface.createFromAsset(assets, fontPathBold)
        tvTitle.typeface = tfBold
        tvDesc.typeface = tf
        btnNo.typeface = tf
        btnYes.typeface = tf


        val adContainer = alertLayout.findViewById<FrameLayout>(R.id.ad_view_container)
        val alert = AlertDialog.Builder(this)
        alert.setView(alertLayout)
        alert.setCancelable(false)
        val dialog = alert.create()
        btnNo.setOnClickListener { dialog.dismiss() }
        btnYes.setOnClickListener {
            ExitSPHelper(this).updateExitCount()
            dialog.dismiss()
            this.finishAffinity()
        }
        dialog.show()

        if (AdsManager(this).isNeedToShowAds()) {
            loadNativeAdvance(adContainer)
        }

    } catch (ignored: Exception) {
        Log.e(TAG, ignored.toString())
    }
}


fun Context.ratingDialog(listener: OnRateListener) {
    try {
        Log.i(TAG, "Rate Dialog called")
        val inflater = (this as Activity).layoutInflater
        val alertLayout = inflater.inflate(R.layout.layout_dialog_rate_us, null)
        val smileRating: SmileRating = alertLayout.findViewById(R.id.rate_smile_rating)
        val btnDismiss = alertLayout.findViewById<TextView>(R.id.rate_btn_dismiss)
        val tvTitle = alertLayout.findViewById<TextView>(R.id.rate_tv_title)
        val tf = Typeface.createFromAsset(assets, fontPath)
        tvTitle.typeface = tf
        btnDismiss.typeface = tf
        val alert = AlertDialog.Builder(this)
        alert.setView(alertLayout)
        alert.setCancelable(false)
        val dialog = alert.create()
        btnDismiss.setOnClickListener {
            ExitSPHelper(this).saveDismissed(true)
            RATING = -1
            dialog.dismiss()
        }
        dialog.setOnDismissListener { listener.onRate(RATING) }
        smileRating.setOnSmileySelectionListener { smiley, _ ->

            dialog.dismiss()
            ExitSPHelper(this).saveRate()
            RATING = smiley
        }
        dialog.show()
    } catch (ignored: Exception) {
        Log.e(TAG, ignored.toString())
    }
}

fun Activity.inAppReview() {
    Log.i(TAG, "inAppReview")
    try {
        val manager: ReviewManager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i(TAG, "task: success")
                // We can get the ReviewInfo object
                val reviewInfo: ReviewInfo = task.result
                val flow: Task<Void> = manager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener { result ->
                    when {
                        result.isSuccessful -> {
                            Log.i(TAG, "result: success: " + result.result)
                        }
                        result.isComplete -> {
                            Log.i(TAG, "result: complete: " + result.result)
                        }
                        else -> {
                            Log.e(TAG, "result: failure: " + result.exception)
                        }
                    }
                }
            } else {
                //Problem in receiving object
                Log.e(TAG, "task: failure")
                rateApp()
            }
        }
    } catch (activityNotFound: ActivityNotFoundException) {
        Log.e(TAG, "Error: $activityNotFound")
        rateApp()
    }
}


fun Context.rateApp() {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    } catch (anfe: ActivityNotFoundException) {
        Log.e(TAG, anfe.toString())
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
    }
}

interface OnRateListener {
    fun onRate(rate: Int)
}
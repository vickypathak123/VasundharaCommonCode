package com.vasu.appcenter.rateandfeedback

import android.app.Activity


const val rateDialogCount = 2

fun Activity.displayExitDialog() {
    val sp = ExitSPHelper(this)
    if (!sp.isRated() && sp.getExitCount() >= rateDialogCount && !sp.isDismissed()) {
        ratingDialog(object : OnRateListener {
            override fun onRate(rate: Int) {
                if (rate >= 4) {
                    rateApp()
                } else if (rate >= 0) {
                    // feedbackDialog()
                    startActivity(FeedbackActivity.newIntent(this@displayExitDialog, rate))
                }
            }
        })
    } else {
        exitDialog()
    }
}
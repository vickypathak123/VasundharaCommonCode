package com.example.appcenter.utilities

import android.os.SystemClock
import android.view.View

abstract class OnSingleClickListener : View.OnClickListener {


   /* private long lastClickTime = 0;

    View.OnClickListener buttonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            // preventing double, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                return;
            }

            lastClickTime = SystemClock.elapsedRealtime();
        }
    }*/

    private var mLastClickTime: Long = 0
    abstract fun onSingleClick(v: View?)
    override fun onClick(v: View) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
        mLastClickTime = currentClickTime
        if (elapsedTime <= MIN_CLICK_INTERVAL) return
        onSingleClick(v)
    }

    companion object {
        private const val MIN_CLICK_INTERVAL: Long = 1500
    }
}
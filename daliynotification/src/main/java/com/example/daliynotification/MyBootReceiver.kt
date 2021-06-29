package com.example.daliynotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("TAG","MyBootReceiver : onReceive ")

        val mSharedPreferences =
            context!!.getSharedPreferences("data", Context.MODE_PRIVATE)


        if(mSharedPreferences.getBoolean("isSetNotification",false)) {
            val edit = mSharedPreferences.edit()
            edit.putBoolean("isSetNotification", false)
            edit.commit()

            NotificationJob.Builder(context)
                    .setTitle(mSharedPreferences.getString("title", "Notification")!!)
                    .setMessage(mSharedPreferences.getString("message", "Notification Test")!!)
                    .setIntervalInDay(mSharedPreferences.getInt("interval", 2))
                    .setHour(mSharedPreferences.getInt("hour", 10))
                    .setMinute(mSharedPreferences.getInt("minute", 0))
                    .setStartActivity(mSharedPreferences.getString("ClassName", "")!!)
                    .setShowLargeIcon(mSharedPreferences.getBoolean("isShowLargeIcon", false))
                    .setLargeIcon(mSharedPreferences.getInt("largeIcon", 0))
                    .setShowBigText(mSharedPreferences.getBoolean("isShowBigText", false))
                    .setIcon(mSharedPreferences.getInt("icon", R.drawable.bell))
                    .build()
        }
    }
}
package com.example.daliynotification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.daliynotification.NotificationJob.Companion.CHANNEL_ID
import com.firebase.jobdispatcher.*
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit


class NotificationJobService : JobService() {

    override fun onStartJob(job: JobParameters): Boolean {
        // Do some work here
        LOGD("onStartJob ${job.extras} ${job.tag} = ${job.trigger}")
        if (job.extras != null) {

            val bundle = job.extras

            var pendingIntent: PendingIntent? = null

            if(bundle!!.getString("ClassName") != null && bundle!!.getString("ClassName") != "") {
                try {
                    val intent =
                        Intent(this, Class.forName(bundle!!.getString("ClassName")!!)).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(bundle!!.getInt("icon"))
                .setContentTitle(bundle!!.getString("title"))
                .setContentText(bundle!!.getString("message"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            if (bundle!!.getBoolean("isShowBigText")) {
                builder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(bundle!!.getString("message"))
                )
            }
            if (bundle!!.getBoolean("isShowLargeIcon")) {
                builder.setLargeIcon(
                    BitmapFactory.decodeResource(
                        applicationContext.resources,
                        bundle!!.getInt("largeIcon")
                    )
                )
            }

            pendingIntent?.let {
                builder.setContentIntent(pendingIntent)
            }


            // notificationId is a unique int for each notification that you must define
            NotificationManagerCompat.from(this).notify(101, builder.build())

            val mSharedPreferences =
                applicationContext.getSharedPreferences("data", Context.MODE_PRIVATE)
            val edit = mSharedPreferences.edit()
            edit.putBoolean("isSetNotification", false)
            edit.apply()
            /*NotificationJob.Builder(applicationContext)
                    .setIntervalInDay(0) // Notifications will be send every 1 day
                    .setHour(13) // Notification hour
                    .setMinute(37) // notification minute
                    .setStartActivity(bundle!!.getString("ClassName")!!) // On click of notification MainActivity will be open
                    .build()*/
            NotificationJob.Builder(applicationContext)
                    .setTitle(mSharedPreferences.getString("title", "Notification")!!)
                    .setMessage(mSharedPreferences.getString("message", "Notification Test")!!)
                    .setIntervalInDay(mSharedPreferences.getInt("interval", 1))
                    .setHour(mSharedPreferences.getInt("hour", 10))
                    .setMinute(mSharedPreferences.getInt("minute", 0))
                    .setStartActivity(mSharedPreferences.getString("ClassName", "")!!)
                    .setShowLargeIcon(mSharedPreferences.getBoolean("isShowLargeIcon", false))
                    .setLargeIcon(mSharedPreferences.getInt("largeIcon", R.drawable.bell))
                    .setShowBigText(mSharedPreferences.getBoolean("isShowBigText", false))
                    .setIcon(mSharedPreferences.getInt("icon", R.drawable.bell))
                    .build()

           /* val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(applicationContext))
            val currentDate = Calendar.getInstance()
            val nextDate = Calendar.getInstance()
            if (mSharedPreferences.getInt("interval", 1) != 0) {
                nextDate.add(Calendar.DATE, mSharedPreferences.getInt("interval", 1))
                nextDate.set(Calendar.HOUR_OF_DAY, mSharedPreferences.getInt("hour", 10))
                nextDate.set(Calendar.MINUTE, mSharedPreferences.getInt("minute", 0))
                nextDate.set(Calendar.SECOND, 0)
                nextDate.set(Calendar.MILLISECOND, 0)
            }

            var second =
                    TimeUnit.MILLISECONDS.toSeconds(nextDate.timeInMillis) - TimeUnit.MILLISECONDS.toSeconds(
                            currentDate.timeInMillis
                    )
            val job = dispatcher.newJobBuilder() //persist the task across boots
                    //call this service when the criteria are met.
                    .setService(NotificationJobService::class.java) //unique id of the task
                    .setTag(mSharedPreferences.getString("unique_tags", "NotificationTestTag")!!) //don't overwrite an existing job with the same tag
                    .setReplaceCurrent(true)
                    .setRecurring(false) // We are mentioning that the job is periodic.
                    .setLifetime(Lifetime.FOREVER)// Run between 30 - 60 seconds from now.
                    .setTrigger(
                            Trigger.executionWindow(
                                    second.toInt(),
                                    second.toInt() + 10
                            )
                    ) // retry with exponential backoff
                    .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL) //.setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .setExtras(bundle)
                    //Run this job only when the network is available.
                    .build()

            if (job != null)
                dispatcher.mustSchedule(job)*/
        }

        return true // Answers the question: "Is there still work going on?"
    }

    override fun onStopJob(job: JobParameters): Boolean {

        return false // Answers the question: "Should this job be retried?"
    }
}
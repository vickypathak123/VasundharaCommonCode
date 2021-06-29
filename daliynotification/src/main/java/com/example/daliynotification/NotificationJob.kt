package com.example.daliynotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.IntRange
import com.firebase.jobdispatcher.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class NotificationJob {


    private var mTextMessage = "Test Notification"
    private var mTextTitle = "Notification"
    private var mIcon = R.drawable.bell
    private var isShowBigText = false
    private var mLargeIcon = R.drawable.bell
    private var isShowLargeIcon = false

    private var mIntervalDay = 0
    private var mHour = 8
    private var mMinute = 0
    private var mBundle: Bundle? = null
    private var isToday = false

    public var mClassName = ""

    private var mSharedPreferences: SharedPreferences? = null

    fun setTitle(title: String): NotificationJob {
        return mNotificationJob!!.apply {
            this.mTextTitle = title
        }
    }

    fun setMessage(message: String): NotificationJob {
        return mNotificationJob!!.apply {
            this.mTextMessage = message
        }
    }

    fun setIcon(icon: Int): NotificationJob {
        return mNotificationJob!!.apply {
            this.mIcon = icon
        }
    }

    fun setShowBigText(show: Boolean): NotificationJob {
        return mNotificationJob!!.apply {
            this.isShowBigText = show
        }
    }

    fun setShowLargeIcon(show: Boolean): NotificationJob {
        return mNotificationJob!!.apply {
            this.isShowLargeIcon = show
        }
    }

    fun setLargeIcon(icon: Int): NotificationJob {
        return mNotificationJob!!.apply {
            this.mLargeIcon = icon
        }
    }


    fun setStartActivity(name: Class<*>): NotificationJob {
        return mNotificationJob!!.apply {
            this.mClassName = name.canonicalName!!
        }
    }


    fun setStartActivity(name: String): NotificationJob {
        return mNotificationJob!!.apply {
            this.mClassName = name
        }
    }


    fun setIntervalInDay(@IntRange(from = 0, to = 6) interval: Int): NotificationJob {
        return mNotificationJob!!.apply {
            this.mIntervalDay = interval
        }
    }

    fun setHour(@IntRange(from = 0, to = 24)hour: Int): NotificationJob {
        return mNotificationJob!!.apply {
            this.mHour = hour
        }
    }

    fun setMinute(@IntRange(from = 0, to = 60)minute: Int): NotificationJob {
        return mNotificationJob!!.apply {
            this.mMinute = minute
        }
    }

    fun setIncludeToday(isToday: Boolean): NotificationJob {
        return mNotificationJob!!.apply {
            this.isToday = isToday
        }
    }

    fun build() {
        if (mContext != null) {
            scheduleJob(mContext!!)
        }
    }

    fun setBundle(bundle: Bundle): NotificationJob {
        mBundle = bundle
        return mNotificationJob!!
    }

    companion object {

        var CHANNEL_ID = "DailyNotification"

        private var mContext: Context? = null
        private var mNotificationJob: NotificationJob? = null

        fun Builder(context: Context): NotificationJob {
            mContext = context
            if (mNotificationJob == null)
                return NotificationJob().apply { mNotificationJob = this }
            return mNotificationJob!!
        }
    }

    fun scheduleJob(context: Context) {

        mSharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        mSharedPreferences?.let {
            if (!it.getBoolean("isSetNotification", false)) {
                val edit = it.edit()
                edit.putBoolean("isSetNotification", true)
                edit.apply()

                createNotificationChannel(context)

                val mSharedPreferences =
                        mContext!!.getSharedPreferences("data", Context.MODE_PRIVATE)


                //creating new firebase job dispatcher
                val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))
                try {
                    dispatcher.cancel(mSharedPreferences.getString("unique_tags", "Notification")!!)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                //creating new job and adding it with dispatcher
                val job: Job? = createJob(context)

                if (job != null)
                    dispatcher.mustSchedule(job)

                Log.d("NotificationJobService", "scheduleJob")


            }
        }


    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification"
            val descriptionText = "Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun getDateCurrentTimeZone(timestamp: Long): String? {
        try {
            /*val calendar = Calendar.getInstance()
            val tz = TimeZone.getDefault()
            calendar.timeInMillis = timestamp
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))*/

            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formattedDate = df.format(timestamp)
            //val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            //val currenTimeZone = calendar.time as Date
            return formattedDate
        } catch (e: Exception) {
        }
        return ""
    }


    private fun createJob(context: Context): Job? {
        val currentDate = Calendar.getInstance()
        Log.d("TAGS", "Time = ${getDateCurrentTimeZone(currentDate.timeInMillis)}")

        val nextDate = Calendar.getInstance()
        Log.d("TAGS", "Time = ${getDateCurrentTimeZone(nextDate.timeInMillis)}")

        val mSharedPreferences =
                mContext!!.getSharedPreferences("data", Context.MODE_PRIVATE)
        val edit = mSharedPreferences.edit()



        mIntervalDay = if (mSharedPreferences.getInt("interval", 0) != 0) mSharedPreferences.getInt("interval", mIntervalDay) else {
            edit.putInt("interval", mIntervalDay)
            edit.commit()
            mIntervalDay
        }
        mHour = if (mSharedPreferences.getInt("hour", 0) != 0) mSharedPreferences.getInt("hour", mHour) else {
            edit.putInt("hour", mHour)
            edit.commit()
            mHour
        }

        mMinute = if (mSharedPreferences.getInt("minute", 0) != 0) mSharedPreferences.getInt("minute", mMinute) else {
            edit.putInt("minute", mMinute)
            edit.commit()
            mMinute
        }

        Log.d("TAGS", "Interval : $mIntervalDay - $mHour - $mMinute == ${mSharedPreferences.getString("unique_tags", "NotificationTestTag")!!} == ${mHour >= currentDate.get(Calendar.HOUR_OF_DAY) && mMinute >= currentDate.get(Calendar.MINUTE)}")




        if (mIntervalDay != 0) {
            if (isToday && mHour >= currentDate.get(Calendar.HOUR_OF_DAY) && mMinute >= currentDate.get(Calendar.MINUTE)) {
                nextDate.set(Calendar.HOUR_OF_DAY, mHour)
                nextDate.set(Calendar.MINUTE, mMinute)
                nextDate.set(Calendar.SECOND, 0)
                nextDate.set(Calendar.MILLISECOND, 0)
            } else {
                nextDate.add(Calendar.DATE, mIntervalDay)
                nextDate.set(Calendar.HOUR_OF_DAY, mHour)
                nextDate.set(Calendar.MINUTE, mMinute)
                nextDate.set(Calendar.SECOND, 0)
                nextDate.set(Calendar.MILLISECOND, 0)
            }
        } else {

            nextDate.set(Calendar.HOUR_OF_DAY, mHour)
            nextDate.set(Calendar.MINUTE, mMinute)
            nextDate.set(Calendar.SECOND, 0)
            nextDate.set(Calendar.MILLISECOND, 0)
        }

        if (nextDate.timeInMillis <= currentDate.timeInMillis) {
            nextDate.add(Calendar.DATE, 1)
        }

        Log.d("TAGS", "Time = ${getDateCurrentTimeZone(nextDate.timeInMillis)}")

        var second =
                TimeUnit.MILLISECONDS.toSeconds(nextDate.timeInMillis) - TimeUnit.MILLISECONDS.toSeconds(
                        currentDate.timeInMillis
                )

        if (second <= 0.toLong()) {
            edit.putBoolean("isSetNotification", false)
            edit.commit()
            return null
        }

        var bundle = Bundle().apply {
            putString("title", mTextTitle)
            putString("message", mTextMessage)
            putInt("icon", mIcon)
            putBoolean("isShowBigText", isShowBigText)
            putInt("largeIcon", mLargeIcon)
            putBoolean("isShowLargeIcon", isShowLargeIcon)
            putString("ClassName", mClassName)
            putBoolean("isToday", isToday)
        }

        edit.apply {
            putString("title", mTextTitle)
            putString("message", mTextMessage)
            putInt("icon", mIcon)
            putBoolean("isShowBigText", isShowBigText)
            putInt("largeIcon", mLargeIcon)
            putBoolean("isShowLargeIcon", isShowLargeIcon)
            putString("ClassName", mClassName)
            putBoolean("isToday", isToday)
            commit()
        }
        Log.d("TAGS", "second $mBundle")
        if (mBundle != null) {
            bundle = mBundle!!
        }

        /*edit.remove("unique_tags")
        edit.commit()*/

        edit.putString("unique_tags", "NotificationTestTag")
        edit.commit()

        Log.d("TAGS", "second $second = ${mSharedPreferences.getString("unique_tags", "NotificationTestTag")!!}")
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))
        return dispatcher.newJobBuilder() //persist the task across boots
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
                // .setConstraints(Constraint.ON_ANY_NETWORK)
                .setExtras(bundle)
                //Run this job only when the network is available.
                .build()

        /*
         return dispatcher.newJobBuilder() //persist the task across boots
                //call this service when the criteria are met.
                .setService(NotificationJobService::class.java) //unique id of the task
                .setTag(mSharedPreferences.getString("unique_tags", "NotificationTest")!!) //don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                .setRecurring(false) // We are mentioning that the job is periodic.
                .setLifetime(Lifetime.FOREVER)// Run between 30 - 60 seconds from now.
                .setTrigger(
                        Trigger.executionWindow(
                                second.toInt(),
                                second.toInt() + 10
                        )
                ) // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR) //.setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setExtras(bundle)
                //Run this job only when the network is available.
                .build()
        * */
    }

    /*private fun updateJob(dispatcher: FirebaseJobDispatcher): Job {
        return dispatcher.newJobBuilder() //update if any task with the given tag exists.
            .setReplaceCurrent(true) //Integrate the job you want to start.
            .setService(NotificationJobService::class.java)
            .setTag("NotificationDemoApp") // Run between 30 - 60 seconds from now.
            .setTrigger(Trigger.executionWindow(30, 60))
            .build()
    }*/

    private fun cancelJob(context: Context?) {
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context!!))
        //Cancel all the jobs for this package
        dispatcher.cancelAll()
        // Cancel the job for this tag

        //dispatcher.cancel("NotificationDemoApp")
    }


}
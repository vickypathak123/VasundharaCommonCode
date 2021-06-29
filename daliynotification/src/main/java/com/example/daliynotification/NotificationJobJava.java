package com.example.daliynotification;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.IntRange;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/*
public class NotificationJobJava {

    private Context mContext;
    private String CHANNEL_ID = "DailyNotification";
    private NotificationJob mNotificationJob = null;

    private String mTextMessage = "Test Notification";
    private String mTextTitle = "Notification";
    private int mIcon = R.drawable.bell;
    private boolean isShowBigText = false;
    private int mLargeIcon = R.drawable.bell;
    private boolean isShowLargeIcon = false;

    private int mIntervalDay = 0;
    private int mHour = 8;
    private int mMinute = 0;
    private Bundle mBundle = null;
    private boolean isToday = false;

    public String mClassName = "";

    private Class<?> activityClass;

    private SharedPreferences mSharedPreferences = null;


    public NotificationJobJava(Context mContext) {
        this.mContext = mContext;
        if (mNotificationJob == null) {
            mNotificationJob = new NotificationJob();
        }
    }


    public void setTitle(String title) {
        this.mTextTitle = title;
        return;
    }

    public void setMessage(String message) {
        this.mTextMessage = message;
    }

    public void setIcon(int icon) {
        this.mIcon = icon;
    }

    public void setShowBigText(Boolean show) {
        this.isShowBigText = show;
    }

    public void setShowLargeIcon(Boolean show) {
        this.isShowLargeIcon = show;
    }

    public void setLargeIcon(int icon) {
        this.mLargeIcon = icon;
    }


    public void setStartActivity(Class<?> activityClass) {

        this.activityClass = activityClass;
    }



    */
/*public void setStartActivity(String name) {
        this.mClassName = name;
    }
*//*


    public void setIntervalInDay(@IntRange(from = 0, to = 6) int interval) {
        this.mIntervalDay = interval;
    }

    public void setHour(@IntRange(from = 0, to = 24) int hour) {
        this.mHour = hour;
    }

    public void setMinute(@IntRange(from = 0, to = 60) int minute) {
        this.mMinute = minute;
    }

    public void setIncludeToday(Boolean isToday) {
        this.isToday = isToday;
    }

    public void build() {
        if (mContext != null) {
            scheduleJob(mContext);
        }
    }

    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }


    public void scheduleJob() {

        mSharedPreferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE)
        if (!mSharedPreferences.getBoolean("isSetNotification", false)) {
            SharedPreferences.Editor edit = mSharedPreferences.edit();
            edit.putBoolean("isSetNotification", true);
            edit.apply();

            createNotificationChannel();


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
            */
/*val calendar = Calendar.getInstance()
            val tz = TimeZone.getDefault()
            calendar.timeInMillis = timestamp
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))*//*


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

        */
/*edit.remove("unique_tags")
        edit.commit()*//*


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

        */
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
        * *//*

    }


}
*/

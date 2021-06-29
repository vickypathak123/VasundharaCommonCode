package com.vasu.appcenter.pushnotifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.appcenter.utilities.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vasu.appcenter.R

/**
 * @author Akshay Harsoda
 * @author Jignesh Patel
 *
 * This class handel your FCM [Firebase Cloud Messaging] Notification content *
 *
 */


/**
 * You can't change this key
 * if you change this key then can't open play store from notification click
 */
const val PACKAGE_NAME = "packageName"
private const val TAG = "MyFirebaseMessagingService"


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onCreate() {
        Log.i(TAG, "onCreate")
    }

    override fun onNewToken(token: String) {
        Log.i(TAG, "onNewToken: $token")
    }

    /**
     * when you app is in foreground state
     * then FCM call this method when notification fire from Firebase console.
     *
     * @param remoteMessage this is FCM Notification data object
     * witch have store all data from Notification
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.i(TAG, "onMessageReceived")
        handleNow(remoteMessage)
    }

    /**
     * this method handle our FCM Notification
     * when your app is in foreground state
     * and fire our local notification
     *
     * @param remoteMessage this is FCM Notification data object
     * witch have store all data from Notification
     */
    private fun handleNow(remoteMessage: RemoteMessage) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            remoteMessage.notification?.let {
                if (remoteMessage.data.containsKey(PACKAGE_NAME)) {
                    fireNotification(remoteMessage)
                }
            }
        }
    }

    /**
     * this method create local notification
     * with FCM notification data
     *
     * @param remoteMessage this is FCM Notification data object
     * witch have store all data from Notification
     */
    private fun fireNotification(remoteMessage: RemoteMessage) {

        Log.i(TAG, "fireNotification")

        val appPackageName = remoteMessage.data[PACKAGE_NAME] // getPackageName()
        val intent = try {
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
        } catch (e: ActivityNotFoundException) {
            Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName"))
        }
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = "Default"
        val mBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle())
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pIntent)

            /**
             * Add this line if you change notification icon in manifest file
             * also check this notification icon
             * is same as in your manifest file notification icon
             */
            .setSmallIcon(R.drawable.ic_launcher_foreground)

            /**
             * Add this line if you change notification icon color in manifest file
             * also check this notification icon color
             * is same as in your manifest file notification icon color
             */
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setAutoCancel(false)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        manager.notify(0, mBuilder.build())
    }
}

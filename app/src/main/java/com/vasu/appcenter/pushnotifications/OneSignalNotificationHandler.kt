package com.vasu.appcenter.pushnotifications

import android.content.Context
import android.util.Log
import com.onesignal.OSNotificationAction
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import com.vasu.appcenter.rateandfeedback.rateApp


private const val TAG = "OneSignal"

class OneSignalNotificationHandler(val mContext: Context) : OneSignal.NotificationOpenedHandler {

    // This fires when a notification is opened by tapping on it.
    override fun notificationOpened(result: OSNotificationOpenResult) {
        val actionType = result.action.type
        val data = result.notification.payload.additionalData

        val isNeedToUpdate = data?.optString("is_need_to_update", null)
        if (isNeedToUpdate != null && isNeedToUpdate.toBoolean()) {
            Log.i(TAG, "is_need_to_update: true")
            mContext.rateApp()
        } else if (isNeedToUpdate != null && !isNeedToUpdate.toBoolean()) {
            Log.i(TAG, "is_need_to_update:false")
        } else {
            Log.i(TAG, "is_need_to_update: null")
        }


        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i(TAG, "Button pressed with id: $result.action.actionID")

        // The following can be used to open an Activity of your choice.
        //   Replace MainActivity with your own Activity class.
        /*
        val intent = Intent(ExampleApplication.instance, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ExampleApplication.instance.startActivity(intent);
        */

        // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
        //  if you are calling startActivity above.
        /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
           */
    }
}
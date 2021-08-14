package com.vasu.appcenter.pushnotifications

import android.content.Context
//import android.util.Log
//import com.onesignal.OSNotificationAction
//import com.onesignal.OSNotificationOpenResult
//import com.onesignal.OneSignal
//import com.vasu.appcenter.rateandfeedback.rateApp


private const val TAG = "OneSignal"

class OneSignalNotificationHandler(val mContext: Context)  {}
    /*: OneSignal.NotificationOpenedHandler {

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
    }
}*/
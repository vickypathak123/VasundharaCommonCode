package com.vasu.appcenter.adshelper

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object EventsHelper {

     fun Context.addEvent(eventName: String) {
         // [START custom_event]
         val params = Bundle()
         params.putString(eventName, eventName)
         FirebaseAnalytics.getInstance(this).logEvent(eventName, params)
         // [END custom_event]
     }
}
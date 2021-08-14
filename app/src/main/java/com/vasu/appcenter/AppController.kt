package com.vasu.appcenter

import androidx.multidex.MultiDexApplication
//import com.onesignal.OneSignal
import com.vasu.appcenter.pushnotifications.OneSignalNotificationHandler
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump


open class AppController :  MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Nexa Regular.otf")
                            .build()
                    )
                )
                .build()
        )


        // OneSignal Initialization
//        OneSignal.startInit(this)
//            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//            .setNotificationOpenedHandler(OneSignalNotificationHandler(this))
//            .unsubscribeWhenNotificationsAreDisabled(true)
//            .init();
    }
}
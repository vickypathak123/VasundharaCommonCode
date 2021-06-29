package com.vasu.appcenter.pushnotifications

/*
Step-1:   First of all, you need to add your project in firebase console first.

Step-2:  Now Adding the Firebase SDK to the App.

Step-3: Add this class path in build.gradle(Project level).
            {
             classpath 'com.google.gms:google-services:4.3.3' // if not exist
            }

Step-4: Add this plugin in build.gradle(App level).
            apply plugin: 'com.google.gms.google-services' // if not exist

Step-5: Add this dependency in build.gradle(App level).
            implementation 'com.google.firebase:firebase-messaging:<latest-one>'

Step-6: Call service from AndroidManifest.xml inside application tag

      <!--Firebase Messaging -->
        <service
            android:name=".firebasemessaging.MyFirebaseMessagingService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>

            <!--- For changing notification icon-->
            <meta-data
                android:name="com.google.firebase.messaging.default_notification_icon"
                android:resource="@drawable/ic_launcher_foreground" />

            <!--- For changing  notification icon color -->
            <meta-data
                android:name="com.google.firebase.messaging.default_notification_color"
                android:resource="@color/colorPrimary" />
        </service>

Step-7 :- call openPlayStoreFromNotificationClick() fun from launcher activity.

*/
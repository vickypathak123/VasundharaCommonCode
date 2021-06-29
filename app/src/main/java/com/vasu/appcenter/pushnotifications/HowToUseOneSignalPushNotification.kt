package com.vasu.appcenter.pushnotifications

/*
Step-1: Add OneSignal Dependencies
            Open your app/build.gradle (Module: app) file, add the following to the very top.

            build.gradle
            buildscript {
                repositories {
                    maven { url 'https://plugins.gradle.org/m2/'}
                }
                dependencies {
                    classpath 'gradle.plugin.com.onesignal:onesignal-gradle-plugin:0.12.8'
                }
            }
            apply plugin: 'com.onesignal.androidsdk.onesignal-gradle-plugin'

            repositories {
                maven { url 'https://maven.google.com' }
            }

Step-2:  Add the following to your app/build.gradle (Module: app) file.
        dependencies {
            implementation 'com.onesignal:OneSignal:3.13.0'
        }

Step-3: Add the following in your android > defaultConfig section.
            Update PUT_YOUR_ONESIGNAL_APP_ID_HERE` with your OneSignal App id.

           android {
               defaultConfig {
                  manifestPlaceholders = [
                      onesignal_app_id: 'PUT_YOUR_ONESIGNAL_APP_ID_HERE',
                      // Project number pulled from dashboard, local value is ignored.
                      onesignal_google_project_number: 'REMOTE'
                  ]
                }
            }

Step-4: Add the following to the onCreate method in your Application class.
            // OneSignal Initialization
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .setNotificationOpenedHandler(OneSignalNotificationHandler(this))
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();


Source : https://documentation.onesignal.com/docs/android-sdk-setup


*/
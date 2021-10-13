package com.vasu.appcenter.akshay.api

enum class APIMethodType {
    GET, POST
}

const val API_CALL_TYPE = "Android"

//<editor-fold desc="For Force Update">
const val INTERFACE_NAME_UPDATE_APK_VERSION = "ApkVersion"
const val FIELD_NAME_PACKAGE_NAME = "packageName"
const val FIELD_NAME_VERSION_CODE = "versionCode"
//</editor-fold>

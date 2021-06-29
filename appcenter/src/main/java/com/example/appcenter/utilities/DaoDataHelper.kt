package com.example.appcenter.utilities

import android.content.Context
import com.example.appcenter.retrofit.model.ModelAppCenter
import com.example.jdrodi.utilities.SPUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * DaoDataHelper.kt - Store model data helper.
 * @author:  Jignesh N Patel
 * @date: 24-Jan-2021 12:12 PM
 */


private const val KEY_APP_CENTER = "key_app_center"


// [START Save & Get App center data]
fun Context.saveAppCenter(appCenterData: String) {
    SPUtil(this).save(KEY_APP_CENTER, appCenterData)
}

fun Context.saveAppCenterModel(modelAppCenter: ModelAppCenter) {
    val modelString = Gson().toJson(modelAppCenter)
    SPUtil(this).save(KEY_APP_CENTER, modelString)
}

fun Context.getAppCenter(): ModelAppCenter? {
    val jsonString = SPUtil(this).getString(KEY_APP_CENTER, "")
    return if (jsonString.isNullOrEmpty() || jsonString.isNullOrBlank()) {
        null
    } else {
        val itemType = object : TypeToken<ModelAppCenter>() {}.type
        Gson().fromJson<ModelAppCenter>(jsonString, itemType)
    }
}
// [END Save & Get App center data]


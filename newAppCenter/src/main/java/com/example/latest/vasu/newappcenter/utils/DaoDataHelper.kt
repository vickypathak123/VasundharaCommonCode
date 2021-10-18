@file:Suppress("unused")

package com.example.latest.vasu.newappcenter.utils

import android.content.Context
import com.example.latest.vasu.newappcenter.model.MoreAppMainModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * DaoDataHelper.kt - Store model data helper.
 * @author:  Jignesh N Patel
 * @date: 24-Jan-2021 12:12 PM
 */


private const val KEY_APP_CENTER = "key_app_center_13_oct_2021"


// [START Save & Get App center data]
fun Context.saveAppCenter(appCenterData: String) {
    this.save(KEY_APP_CENTER, appCenterData)
}

fun Context.saveAppCenterModel(modelAppCenter: MoreAppMainModel) {
    val modelString = Gson().toJson(modelAppCenter)
    this.save(KEY_APP_CENTER, modelString)
}

fun Context.getAppCenter(): MoreAppMainModel? {
    val jsonString = this.getString(KEY_APP_CENTER, "")
    return if (jsonString.isEmpty() || jsonString.isBlank()) {
        null
    } else {
        val itemType = object : TypeToken<MoreAppMainModel>() {}.type
        Gson().fromJson<MoreAppMainModel>(jsonString, itemType)
    }
}
// [END Save & Get App center data]


package com.vasu.appcenter.updatejsonparsing

import android.content.Context
import com.example.jdrodi.utilities.SPUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vasu.appcenter.retrofit.model.ForceUpdateModel

/**
 * JsonUtil.kt - Utilities of json parsing.
 * @author:  Jignesh N Patel
 * @date: 04-Feb-2021 2:51 PM
 */
private const val KEY_FORCE_UPDATE = "key_force_update"


// [START Save & Get force update]
fun Context.saveForceUpdate(appCenterData: String) {
    SPUtil(this).save(KEY_FORCE_UPDATE, appCenterData)
}

fun Context.saveForceUpdateModel(modelAppCenter: ForceUpdateModel) {
    val modelString = Gson().toJson(modelAppCenter)
    SPUtil(this).save(KEY_FORCE_UPDATE, modelString)
}

fun Context.getForceUpdate(): ForceUpdateModel? {
    val jsonString = SPUtil(this).getString(KEY_FORCE_UPDATE, "")
    return if (jsonString.isNullOrEmpty() || jsonString.isNullOrBlank()) {
        null
    } else {
        val itemType = object : TypeToken<ForceUpdateModel>() {}.type
        Gson().fromJson<ForceUpdateModel>(jsonString, itemType)
    }
}
// [END Save & Get force update]


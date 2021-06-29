package com.vasu.appcenter.adshelper

import android.content.Context
import com.google.android.gms.ads.nativead.NativeAd
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val KEY_NATIVE="native_ads"

class AdsManager(mActivity: Context) {

    // SP to be save & retrieve
    private val isNeedToShow = "isNeedToShow"
    private val sp: SharedPreferences = SharedPreferences(mActivity)

    fun onProductPurchased() {
        sp.save(isNeedToShow, true)
    }

    fun isNeedToShowAds(): Boolean {
        val isProductPurchased = sp.read(isNeedToShow, false)
        return !isProductPurchased
    }

    // [START Save & Get NativeAd]
    fun saveNativeAd(nativeAd: NativeAd) {
        val nativeAdString = Gson().toJson(nativeAd)
        sp.save(KEY_NATIVE, nativeAdString)
    }

    fun getNativeAd(): NativeAd? {
        val jsonString = sp.read(KEY_NATIVE, "")
        return if (jsonString.isNullOrEmpty() || jsonString.isNullOrBlank()) {
            null
        } else {
            val itemType = object : TypeToken<NativeAd>() {}.type
            Gson().fromJson<NativeAd>(jsonString, itemType)
        }
    }
    // [END Save & Get NativeAd]

/*

    // [START Save & Get Cover Category]
    fun Context.saveCoverCategoryImages(categoryId: String, coverCategorImages: List<CoverCategoryImages>) {
        val jsonString = Gson().toJson(coverCategorImages)
        SPUtil(this).save(categoryId, jsonString)
    }

    fun Context.getCoverCategoryImages(categoryId: String): List<CoverCategoryImages> {
        val jsonString = SPUtil(this).getString(categoryId, "")
        return if (jsonString.isNullOrEmpty() || jsonString.isNullOrBlank()) {
            emptyList()
        } else {
            val itemType = object : TypeToken<List<CoverCategoryBackgrounds>>() {}.type
            Gson().fromJson(jsonString, itemType)
        }
    }
// [END Save & Get Cover Category]
*/













    /**
     *   SharedPreferences helper class
     */
    private inner class SharedPreferences//  Default constructor
    internal constructor(private val mActivity: Context) {
        private val myPreferences = "ads_pref"

        //  Save boolean value
        fun save(key: String, value: Boolean) {
            val editor = mActivity.getSharedPreferences(myPreferences, 0).edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        //  Read Boolean value
        fun read(key: String, defValue: Boolean): Boolean {
            return mActivity.getSharedPreferences(myPreferences, 0).getBoolean(key, defValue)
        }

        //  Save String value
        fun save(key: String, value: String) {
            val editor = mActivity.getSharedPreferences(myPreferences, 0).edit()
            editor.putString(key, value)
            editor.apply()
        }

        //  Read String value
        fun read(key: String, defValue: String): String? {
            return mActivity.getSharedPreferences(myPreferences, 0).getString(key, defValue)
        }
    }
}
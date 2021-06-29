package com.example.appcenter.retrofit.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelAppCenter(
    @SerializedName("status")
    @Expose var status: Int,

    @SerializedName("is_home_enable")
    @Expose var is_home_enable: Boolean,

    @SerializedName("message")
    @Expose var message: String,
    @SerializedName("native_add")
    @Expose var native_add: NativeAdd,
    @SerializedName("translator_ads_id")
    @Expose var translator_ads_id: TranslatorAdsId,
    @SerializedName("data")
    @Expose var data: List<Data>,
    @SerializedName("app_center")
    @Expose var app_center: List<AppCenter>,
    @SerializedName("home")
    @Expose var home: List<Home>,
    @SerializedName("more_apps")
    @Expose var more_apps: List<MoreApps>
) : Parcelable

@Parcelize
data class NativeAdd(
    @SerializedName("id")
    @Expose var id: Int,
    @SerializedName("position")
    @Expose var position: Int,
    @SerializedName("image")
    @Expose var image: String,
    @SerializedName("playstore_link")
    @Expose var playstore_link: String,
    @SerializedName("package_name")
    @Expose var package_name: String,
    @SerializedName("is_active")
    @Expose var is_active: Int,
    @SerializedName("image_active")
    @Expose var image_active: Int
) : Parcelable

@Parcelize
data class TranslatorAdsId(
    @SerializedName("banner")
    @Expose var banner: String,
    @SerializedName("interstitial")
    @Expose var interstitial: String
) : Parcelable

@Parcelize
data class Data(
    @SerializedName("id")
    @Expose var id: Int,
    @SerializedName("app_id")
    @Expose var app_id: Int,
    @SerializedName("position")
    @Expose var position: Int,
    @SerializedName("name")
    @Expose var name: String,
    @SerializedName("thumb_image")
    @Expose var thumb_image: String,
    @SerializedName("app_link")
    @Expose var app_link: String,
    @SerializedName("package_name")
    @Expose var package_name: String,
    @SerializedName("full_thumb_image")
    @Expose var full_thumb_image: String,
    @SerializedName("splash_active")
    @Expose var splash_active: Int
) : Parcelable

@Parcelize
data class AppCenter(
    @SerializedName("id")
    @Expose var id: Int,
    @SerializedName("position")
    @Expose var position: Int,
    @SerializedName("name")
    @Expose var name: String,
    @SerializedName("is_active")
    @Expose var is_active: Int,
    @SerializedName("catgeory")
    @Expose var catgeory: String,
    @SerializedName("sub_category")
    @Expose var sub_category: List<SubCategory>
) : Parcelable

@Parcelize
data class Home(
    @SerializedName("id")
    @Expose var id: Int,
    @SerializedName("position")
    @Expose var position: Int,
    @SerializedName("name")
    @Expose var name: String,
    @SerializedName("is_active")
    @Expose var is_active: Int,
    @SerializedName("catgeory")
    @Expose var catgeory: String,
    @SerializedName("sub_category")
    @Expose var sub_category: List<SubCategory>
) : Parcelable

@Parcelize
data class MoreApps(
    @SerializedName("id")
    @Expose var id: Int,
    @SerializedName("position")
    @Expose var position: Int,
    @SerializedName("name")
    @Expose var name: String,
    @SerializedName("is_active")
    @Expose var is_active: Int,
    @SerializedName("catgeory")
    @Expose var catgeory: String,
    @SerializedName("sub_category")
    @Expose var sub_category: List<SubCategory>
) : Parcelable

@Parcelize
data class SubCategory(
    @SerializedName("id")
    @Expose var id: Int = 0,
    @SerializedName("app_id")
    @Expose var app_id: Int = 0,
    @SerializedName("position")
    @Expose var position: Int = 0,
    @SerializedName("name")
    @Expose var name: String? = null,
    @SerializedName("icon")
    @Expose var icon: String? = null,
    @SerializedName("star")
    @Expose var star: String? = null,
    @SerializedName("installed_range")
    @Expose var installed_range: String? = null,
    @SerializedName("app_link")
    @Expose var app_link: String? = null,
    @SerializedName("banner")
    @Expose var banner: String? = null,
    @SerializedName("is_active")
    @Expose var is_active: Int = 0,
    @SerializedName("image_active")
    @Expose var image_active: Int = 0,
    @SerializedName("banner_image")
    @Expose var banner_image: String? = null
) : Parcelable
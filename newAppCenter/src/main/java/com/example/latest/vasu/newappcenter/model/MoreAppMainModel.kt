package com.example.latest.vasu.newappcenter.model


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MoreAppMainModel(
    @SerializedName("app_center")
    @Expose
    val appCenter: ArrayList<AppCenter>,
    @SerializedName("data")
    @Expose
    val data: ArrayList<Data>,
    @SerializedName("home")
    @Expose
    val home: ArrayList<Home>,
    @SerializedName("is_home_enable")
    @Expose
    val isHomeEnable: Boolean,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("more_apps")
    @Expose
    val moreApps: ArrayList<MoreApps>,
    @SerializedName("native_add")
    @Expose
    val nativeAdd: NativeAdd,
    @SerializedName("status")
    @Expose
    val status: Int,
    @SerializedName("translator_ads_id")
    @Expose
    val translatorAdsId: TranslatorAdsId
) : Parcelable

@Keep
@Parcelize
data class AppCenter(
    @SerializedName("catgeory")
    @Expose
    val category: String,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("is_active")
    @Expose
    val isActive: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("position")
    @Expose
    val position: Int,
    @SerializedName("status")
    @Expose
    val status: Int,
    @SerializedName("sub_category")
    @Expose
    val subCategory: ArrayList<SubCategory>
) : Parcelable

@Keep
@Parcelize
data class Data(
    @SerializedName("app_id")
    @Expose
    val appId: Int,
    @SerializedName("app_link")
    @Expose
    val appLink: String,
    @SerializedName("full_thumb_image")
    @Expose
    val fullThumbImage: String,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("package_name")
    @Expose
    val packageName: String,
    @SerializedName("position")
    @Expose
    val position: Int,
    @SerializedName("splash_active")
    @Expose
    val splashActive: Int,
    @SerializedName("thumb_image")
    @Expose
    val thumbImage: String
) : Parcelable

@Keep
@Parcelize
data class Home(
    @SerializedName("catgeory")
    @Expose
    val category: String,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("is_active")
    @Expose
    val isActive: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("position")
    @Expose
    val position: Int,
    @SerializedName("status")
    @Expose
    val status: Int,
    @SerializedName("sub_category")
    @Expose
    val subCategory: ArrayList<SubCategory>
) : Parcelable

@Keep
@Parcelize
data class MoreApps(
    @SerializedName("id")
    @Expose var id: Int,
    @SerializedName("position")
    @Expose var position: Int,
    @SerializedName("name")
    @Expose var name: String,
    @SerializedName("is_active")
    @Expose var isActive: Int,
    @SerializedName("catgeory")
    @Expose var catgeory: String,
    @SerializedName("sub_category")
    @Expose var subCategory: List<SubCategory>
) : Parcelable

@Keep
@Parcelize
data class NativeAdd(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("image_active")
    @Expose
    val imageActive: Int,
    @SerializedName("is_active")
    @Expose
    val isActive: Int,
    @SerializedName("package_name")
    @Expose
    val packageName: String,
    @SerializedName("playstore_link")
    @Expose
    val playStoreLink: String,
    @SerializedName("position")
    @Expose
    val position: Int
) : Parcelable

@Keep
@Parcelize
data class TranslatorAdsId(
    @SerializedName("banner")
    @Expose
    val banner: String,
    @SerializedName("interstitial")
    @Expose
    val interstitial: String
) : Parcelable

@Keep
@Parcelize
data class SubCategory(
    @SerializedName("app_id")
    @Expose
    val appId: Int,
    @SerializedName("app_link")
    @Expose
    val appLink: String,
    @SerializedName("banner")
    @Expose
    val banner: String,
    @SerializedName("banner_image")
    @Expose
    val bannerImage: String,
    @SerializedName("icon")
    @Expose
    val icon: String,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("image_active")
    @Expose
    val imageActive: Int,
    @SerializedName("installed_range")
    @Expose
    val installedRange: String,
    @SerializedName("is_active")
    @Expose
    val isActive: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("position")
    @Expose
    val position: Int,
    @SerializedName("star")
    @Expose
    val star: String
) : Parcelable
package com.vasu.appcenter.inapp

import android.content.Context
import com.android.billingclient.api.Purchase
import com.example.jdrodi.utilities.OnPositive
import com.example.jdrodi.utilities.showAlert
import com.vasu.appcenter.R
import com.vasu.appcenter.utilities.fontPath
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull


const val PLAY_STORE_SUBSCRIPTION_URL = "https://play.google.com/store/account/subscriptions"
const val PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL = "https://play.google.com/store/account/subscriptions?sku=%s&package=%s"

// Products Id's
const val PRODUCT_PURCHASED = "android.test.purchased"
const val PRODUCT_CANCELED = "android.test.canceled"
const val PRODUCT_REFUNDED = "android.test.refunded"
const val PRODUCT_UNAVAILABLE = "android.test.item_unavailable"

// List of purchased products
val purchaseHistory: ArrayList<Purchase> = ArrayList()

// List of available products to buy
val PRODUCT_LIST: ArrayList<DaoProducts> = ArrayList()

// SKU list
val skuList = listOf(
    PRODUCT_PURCHASED,
    PRODUCT_CANCELED,
    PRODUCT_REFUNDED,
    PRODUCT_UNAVAILABLE
)

fun Context.showPurchaseAlert(@NotNull productId: String, fIsConsumable: Boolean) {
    val title = getString(R.string.app_name)
    val message = getString(R.string.ask_remove_ads)
    val positiveBtn = resources.getString(R.string.dialog_yes)
    val negativeBtn = resources.getString(R.string.dialog_no)
    showAlert(title, message, positiveBtn, negativeBtn, fontPath, object : OnPositive {
        override fun onYes() {
            GlobalScope.launch {
                InAppPurchaseHelper.instance!!.purchaseProduct(productId, fIsConsumable)
            }
        }
    })
}


fun Context.showPurchaseSuccess() {
    val title = getString(R.string.app_name)
    val message = resources.getString(R.string.remove_ads)
    val positiveBtn = resources.getString(R.string.dialog_ok)
    showAlert(title, message, positiveBtn, null, fontPath, object : OnPositive {
        override fun onYes() {

        }
    })

}

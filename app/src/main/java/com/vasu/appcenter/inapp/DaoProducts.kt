package com.vasu.appcenter.inapp

import com.android.billingclient.api.SkuDetails

data class DaoProducts(val productId: String, val priceOfProduct: String, val currencyCode: String, val skuDetails: SkuDetails)
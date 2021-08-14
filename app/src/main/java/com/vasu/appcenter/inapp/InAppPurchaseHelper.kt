package com.vasu.appcenter.inapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.*
import com.vasu.appcenter.adshelper.AdsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.NotNull


private const val TAG = "IN_APP_BILLING"

@Suppress("unused")
class InAppPurchaseHelper {

    private lateinit var mActivity: Activity // Context of Activity
    private lateinit var billingClient: BillingClient // Object of BillingClient
    lateinit var onPurchased: OnPurchased // Callback for listen purchase states
    private var isConsumable: Boolean = false // Flag if purchase need to consume so user can buy again


    // get Instance of InAppPurchaseHelper class
    companion object {
        var instance: InAppPurchaseHelper? = null
            get() {
                if (field == null) {
                    synchronized(InAppPurchaseHelper::class.java) {
                        if (field == null) {
                            field = InAppPurchaseHelper()
                        }
                    }
                }
                return field
            }
            private set
    }

    // variable to track event time
    var mLastClickTime: Long = 0
    val mMinDuration = 1000


    /**
     * ToDo: To receive a callback once the setup of the billing client is complete
     * */
    private val purchaseUpdateListener = PurchasesUpdatedListener { billingResult, purchases ->


        if (SystemClock.elapsedRealtime() - mLastClickTime < mMinDuration) {

        } else {
            mLastClickTime = SystemClock.elapsedRealtime()

            // To be implemented in a later section.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                Log.i(TAG, "purchaseUpdateListener | ITEM_ALREADY_OWNED")
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                Toast.makeText(mActivity, "You've cancelled the Google play billing process", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mActivity, "Item not found or Google play billing error", Toast.LENGTH_SHORT).show()
                logResponseCode("Purchase Update Listener", billingResult.responseCode)
            }
        }

    }

    /**
     * ToDo: To establish a connection to Google Play Billing.
     *
     * @param fActivity The Context of activity
     * @param purchaseListener The Callback to indicate user about purchase state
     * */
    fun initBillingClient(fActivity: Activity, purchaseListener: OnPurchased): BillingClient {
        mActivity = fActivity
        onPurchased = purchaseListener
        billingClient = BillingClient.newBuilder(mActivity).enablePendingPurchases().setListener(purchaseUpdateListener).build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.i(TAG, "startConnection | RESULT OK")
                    onPurchased.onBillingSetupFinished(billingResult)
                } else {
                    // Billing Unavailable may be network issue or something else.
                    onPurchased.onBillingUnavailable()
                    logResponseCode("startConnection", billingResult.responseCode)
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to Google Play by calling the startConnection() method.
                Log.e(TAG, " onBillingServiceDisconnected | DISCONNECTED")
            }
        })

        return billingClient
    }

    /**
     * ToDo: To get SKU details and Purchased history of product
     * */
    suspend fun initProducts() {
        val params = SkuDetailsParams
            .newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)
            .build()

        if (billingClient.isReady) {
            // Retrieve a value for "skuDetails" by calling querySkuDetails().
            withContext(Dispatchers.IO) {
                val skuDetails = billingClient.querySkuDetails(params)
                if (skuDetails.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val listOfSku = skuDetails.skuDetailsList!!
                    for (skuD in listOfSku) {
                        val sku = skuD.sku
                        val price = skuD.price
                        val cc = skuD.priceCurrencyCode
                        val productInfo = DaoProducts(sku, price, cc, skuD)
                        PRODUCT_LIST.add(productInfo)
                        Log.i(TAG, "initProducts | $sku : $price : $cc")
                    }
                } else {
                    logResponseCode("Init Products Price", skuDetails.billingResult.responseCode)
                }

                // Retrieve a value for "Purchases History" by calling queryPurchases().
                val purchasedHistoryResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
                if (purchasedHistoryResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val purchasedHistory = purchasedHistoryResult.purchasesList!!
                    if (purchasedHistory.isNotEmpty()) {

                        // If Any Purchase product found the disable ads
                        AdsManager(mActivity).onProductPurchased()


                        Log.i(TAG, "************* Purchase History *************")
                        for (purchase in purchasedHistory) {
                            purchaseHistory.add(purchase)
                            logPurchaseItem(purchase)
                        }
                        Log.i(TAG, "********* End of Purchase History *********")
                    } else {
                        Log.i(TAG, "initProducts | Purchase History Not Found")
                    }
                } else {
                    logResponseCode("Init Products History", purchasedHistoryResult.responseCode)
                }
            }
            Log.i(TAG, "initProducts | Done")
        } else {
            Log.i(TAG, "initProducts | The billing client is not ready")
        }
    }


    /**
     * ToDo: To purchase product
     *  @param productId The ID to purchase the product
     *  @param fIsConsumable The flag if purchase need to consume or not
     * */
    suspend fun purchaseProduct(@NotNull productId: String, fIsConsumable: Boolean) {

        isConsumable = fIsConsumable

        if (billingClient.isReady) {
            val params = SkuDetailsParams
                .newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.INAPP)
                .build()

            val skuDetailsResult = withContext(Dispatchers.IO) {
                billingClient.querySkuDetails(params)
            }

            val skuList = skuDetailsResult.skuDetailsList!!
            val skuDetail = getSkuDetails(productId, skuList)
            if (skuDetail != null) {
                val billingFlowParams = BillingFlowParams
                    .newBuilder()
                    .setSkuDetails(skuDetail)
                    .build()
                val billingResult = billingClient.launchBillingFlow(mActivity, billingFlowParams)

                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                        Log.i(TAG, "purchaseProduct | ITEM_ALREADY_OWNED")

                        // If product purchased successfully disable ads
                        AdsManager(mActivity).onProductPurchased()

                        onPurchased.onProductAlreadyOwn()
                        logSkuDetails(skuDetail)
                    }
                    BillingClient.BillingResponseCode.OK -> {
                        Log.i(TAG, "purchaseProduct | Purchase in Progress")
                    }
                    else -> {
                        logResponseCode("purchaseProduct", billingResult.responseCode)
                    }
                }
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    onPurchased.onBillingKeyNotFound(productId)
                }
                Log.i(TAG, "purchaseProduct | SKU Detail not found for product id: $productId")
            }

        } else {
            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(mActivity, "The billing client is not ready", Toast.LENGTH_SHORT).show()
            }
            Log.i(TAG, "purchaseProduct | The billing client is not ready")
        }
    }


    /**
     * ToDo: Verify the purchase acknowledge & consume purchase
     *  @param purchase The purchased product
     * */
    private fun handlePurchase(purchase: Purchase) {
        // Verify the purchase.

        // If product purchased successfully disable ads
        AdsManager(mActivity).onProductPurchased()

        onPurchased.onPurchasedSuccess(purchase)

        GlobalScope.launch(Dispatchers.Main) {
            acknowledgePurchase(purchase)
            if (isConsumable) {
                consumePurchase(purchase)
            } else {
                Log.i(TAG, "Consume Purchase | False")
            }
        }
    }

    /**
     * ToDo: Consume purchase for buy again. This method make the one-time product available for purchase again.
     *  @param purchase The purchased product
     * */
    private suspend fun consumePurchase(purchase: Purchase) {
        Log.i(TAG, "Consume Purchase | Begin")
        val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
        val consumeResult = billingClient.consumePurchase(consumeParams)
        if (consumeResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            Log.i(TAG, "Consume Purchase | OK")
        } else {
            logResponseCode("Consume Purchase", consumeResult.billingResult.responseCode)
        }
    }


    /**
     * ToDo: Acknowledge a purchase
     *  @param purchase The purchased product
     * */
    private suspend fun acknowledgePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            Log.i(TAG, "Acknowledge Purchase | Begin")
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                val ackPurchaseResult = withContext(Dispatchers.IO) {
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
                }

                if (ackPurchaseResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.i(TAG, "Acknowledge Purchase | OK")
                } else {
                    logResponseCode("Acknowledge Purchase", ackPurchaseResult.responseCode)
                }
            }
        } else {
            Log.i(TAG, "Acknowledge Purchase | Product not purchased")
        }

        logPurchaseItem(purchase)
    }


    fun openPlayStoreSubscriptions(sku: String?) {
        Log.i(TAG, "Viewing subscriptions on the Google Play Store")

        val url = if (sku == null) {
            // If the SKU is not specified, just open the Google Play subscriptions URL.
            PLAY_STORE_SUBSCRIPTION_URL
        } else {
            // If the SKU is specified, open the deeplink for this SKU on Google Play.
            java.lang.String.format(PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL, sku, mActivity.packageName)
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        mActivity.startActivity(intent)
    }

    /**
     * ToDo: To get price of product
     *
     * @param productId The ID of the product
     *  @return Price of product
     * */
    fun getProductPrice(@NotNull productId: String): String {
        for (product in PRODUCT_LIST) {
            if (product.productId == productId) {
                return product.priceOfProduct
            }
        }
        return "Not Found"
    }

    /**
     * ToDo: To get SKU details by product id from the list
     *
     * @param productId The ID of the product
     * @param skuList List of SKU's
     *  @return The SKU matching to the product id
     * */
    private fun getSkuDetails(@NotNull productId: String, skuList: List<SkuDetails>): SkuDetails? {
        for (skuDetail in skuList) {
            if (skuDetail.sku == productId) {
                return skuDetail
            }
        }

        return null
    }

    /**
     * ToDo: To get SKU details by product id from the list save on initProduct()
     *
     * @param productId The ID of the product
     *  @return The SKU matching to the product id
     * */
    private fun getSkuDetails(@NotNull productId: String): SkuDetails? {
        for (product in PRODUCT_LIST) {
            if (product.productId == productId) {
                return product.skuDetails
            }
        }
        return null
    }


    /**
     * ToDo: To get purchase state in string from id
     *
     * @param purchaseState Purchase state id
     * @return purchase state string
     * */
    private fun getPurchaseState(purchaseState: Int): String {
        return when (purchaseState) {
            0 -> {
                "UNSPECIFIED_STATE"
            }
            1 -> {
                "PURCHASED"
            }
            2 -> {
                "PENDING"
            }
            else -> {
                "Unknown"
            }
        }
    }


    /**
     * ToDo: To print log of purchase
     *
     * @param purchase The product purchase
     * */
    private fun logPurchaseItem(purchase: Purchase) {
        Log.i(TAG, "==================  Purchase Details ==================")
        Log.i(TAG, "Order ID: " + purchase.orderId)
        Log.i(TAG, "Original JSON: " + purchase.originalJson)
        Log.i(TAG, "Package Name: " + purchase.packageName)
        Log.i(TAG, "Purchase Token: " + purchase.purchaseToken)
        Log.i(TAG, "Signature: " + purchase.signature)
//        Log.i(TAG, "SKU: " + purchase.sku)
//        Log.i(TAG, "Price: " + getProductPrice(purchase.sku))
        Log.i(TAG, "Purchase State: " + getPurchaseState(purchase.purchaseState))
        Log.i(TAG, "Purchase Time: " + purchase.purchaseTime)
        Log.i(TAG, "Is Acknowledge: " + purchase.isAcknowledged)
        Log.i(TAG, "Is Auto Renewing: " + purchase.isAutoRenewing)
        Log.i(TAG, "==============  End of Purchase Details ==============")
    }


    /**
     * ToDo: To print log of SkuDetails
     *
     * @param skuD The SkuDetails
     * */
    private fun logSkuDetails(skuD: SkuDetails?) {
        Log.i(TAG, "==================  SKU Details ==================")
        Log.i(TAG, "SKU: " + skuD!!.sku)
        Log.i(TAG, "Title: " + skuD.title)
        Log.i(TAG, "Description: " + skuD.description)
        Log.i(TAG, "Free trial period: " + skuD.freeTrialPeriod)
        Log.i(TAG, "Price: " + skuD.price)
        Log.i(TAG, "Original Price: " + skuD.originalPrice)
        Log.i(TAG, "Original Json: " + skuD.originalJson)
        Log.i(TAG, "Type: " + skuD.type)
        Log.i(TAG, "Icon Url: " + skuD.iconUrl)
        Log.i(TAG, "=============== End of SKU Details ===============")
    }


    /**
     * ToDo: To print log of SkuDetails
     *
     * @param responseMsg Prefix of the log
     * @param responseCode The response code
     * */
    private fun logResponseCode(responseMsg: String, @NotNull responseCode: Int) {
        var msg = "OK"
        when (responseCode) {

            BillingClient.BillingResponseCode.OK -> {
                msg = "OK"
            }

            BillingClient.BillingResponseCode.ERROR -> {
                msg = "ERROR"
            }
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {
                msg = "BILLING_UNAVAILABLE"
            }
            BillingClient.BillingResponseCode.DEVELOPER_ERROR -> {
                msg = "DEVELOPER_ERROR"
            }
            BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED -> {
                msg = "FEATURE_NOT_SUPPORTED"
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                msg = "ITEM_ALREADY_OWNED"
            }
            BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
                msg = "ITEM_NOT_OWNED"
            }
            BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> {
                msg = "ITEM_UNAVAILABLE"
            }
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> {
                msg = "SERVICE_DISCONNECTED"
            }
            BillingClient.BillingResponseCode.SERVICE_TIMEOUT -> {
                msg = "SERVICE_TIMEOUT"
            }
            BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> {
                msg = "SERVICE_UNAVAILABLE"
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                msg = "USER_CANCELED"
            }
        }
        Log.e(TAG, "$responseMsg : $msg")
    }


    /**
     * ToDo: Callback on purchase
     * */

    interface OnPurchased {
        fun onPurchasedSuccess(purchase: Purchase)
        fun onProductAlreadyOwn()
        fun onBillingSetupFinished(billingResult: BillingResult)
        fun onBillingUnavailable()
        fun onBillingKeyNotFound(productId: String)
    }


}
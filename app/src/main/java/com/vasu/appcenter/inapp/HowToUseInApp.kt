package com.vasu.appcenter.inapp

/**

 ToDo: Steps for Add Google Play Billing and init Billing Client on startup

Step - 1: Add the Google Play Billing Library dependency to your app's build.gradle file as shown:
        dependencies {
        def billing_version = "3.0.0" // use latest version
        implementation 'com.android.billingclient:billing-ktx:$billing_version'
        }

 Step - 2: Add Billing and internet permission in AndroidManifest.xml file as shown:

        <uses-permission android:name="com.android.vending.BILLING" />
        <uses-permission android:name="android.permission.INTERNET" />

Step - 3 : Create product keys in InAppConstants.kt file as shown:

        // Products Id's
        const val PRODUCT_PURCHASED = "android.test.purchased"
        const val PRODUCT_CANCELED = "android.test.canceled"

Step - 4 : Add created product keys in skuList in InAppConstants.kt file as shown:

        // SKU list
        val skuList = listOf(
        PRODUCT_PURCHASED,
        PRODUCT_CANCELED
        )

Step - 5: Implement  InAppPurchaseHelper.OnPurchased in your launcher activity to get callback

Step - 6: Call initBillingClient to establish a connection to Google Play Billing. once connection done either it will call
          onBillingSetupFinished or onBillingUnavailable

        override fun onCreate(savedInstanceState: Bundle?) {
             super.onCreate(savedInstanceState)

            InAppPurchaseHelper.instance!!.initBillingClient(mActivity!!, this)

        }

 Step - 7 If Connection establish successfully then call initProducts() as shown:
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                GlobalScope.launch(Dispatchers.Main) {
                    InAppPurchaseHelper.instance!!.initProducts()
                    Log.i(TAG, "IN_APP_BILLING | Done")
                }
            }

 ToDo: Steps for Buy product on specific activity or fragments

 Step - 1: Implement  InAppPurchaseHelper.OnPurchased in your launcher activity to get callback

         override fun onPurchasedSuccess(purchase: Purchase) {
                 Log.i(TAG, "purchase")
                 showPurchaseSuccess()
                 removeAds()
         }

         override fun onProductAlreadyOwn() {
                 Log.i(TAG, "onProductAlreadyOwn")
                 showPurchaseSuccess()
                 removeAds()
         }


         override fun onBillingSetupFinished(billingResult: BillingResult) {

         }

         override fun onBillingUnavailable() {

         }

         override fun onBillingKeyNotFound(productId: String) {
                 val message = "SKU Detail not found for product id: $productId"
                 toast(message)
         }

 Step - 2: Call initBillingClient to establish a connection to Google Play Billing if need to Buy product as Shown. once connection done either it will call
 onBillingSetupFinished or onBillingUnavailable


        AdsManager(mActivity!!).isNeedToShowAds() return true if is need to show add or not and it is automatically managed inside
                                                            InAppPurchaseHelper.kt internally

         override fun onCreate(savedInstanceState: Bundle?) {
             super.onCreate(savedInstanceState)

                 if (AdsManager(mActivity!!).isNeedToShowAds()) {
                         InAppPurchaseHelper.instance!!.initBillingClient(mActivity!!, this)
                 }
         }

 Step - 3 : To buy product call purchaseProduct with product id and consumable flag as shown:
                Product id: Id of the product that you want to buy
                iSConsumable: True for consuming product so user can purchase again otherwise false

             *.setOnClickListener {
                    showPurchaseAlert(PRODUCT_PURCHASED, false)
             }




 */
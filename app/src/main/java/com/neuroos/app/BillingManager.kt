package com.neuroos.app

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BillingManager(private val context: Context) : PurchasesUpdatedListener {

    private val billingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    private val _isPremium = MutableStateFlow(false)
    val isPremium = _isPremium.asStateFlow()

    init {
        startConnection()
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryPurchases()
                }
            }
            override fun onBillingServiceDisconnected() {}
        })
    }

    fun queryPurchases() {
        val subParams = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient.queryPurchasesAsync(subParams) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val premiumActive = purchases.any { purchase ->
                    purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                            purchase.products.contains(NeuroProducts.PREMIUM_MONTHLY)
                }
                if (premiumActive) {
                    _isPremium.value = true
                } else {
                    // Check lifetime (One-time purchase)
                    val inAppParams = QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                    billingClient.queryPurchasesAsync(inAppParams) { inAppResult, inAppPurchases ->
                        if (inAppResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            _isPremium.value = inAppPurchases.any { 
                                it.purchaseState == Purchase.PurchaseState.PURCHASED &&
                                it.products.contains(NeuroProducts.PREMIUM_LIFETIME)
                            }
                        }
                    }
                }
            }
        }
    }

    fun launchPurchaseFlow(activity: Activity, isLifetime: Boolean = false) {
        val productType = if (isLifetime) BillingClient.ProductType.INAPP else BillingClient.ProductType.SUBS
        val productId = if (isLifetime) NeuroProducts.PREMIUM_LIFETIME else NeuroProducts.PREMIUM_MONTHLY

        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(productType)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, queryResult ->
            val detailsList = queryResult.productDetailsList
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && detailsList != null && detailsList.isNotEmpty()) {
                val productDetails = detailsList[0]
                
                val productDetailsParamsList = if (productType == BillingClient.ProductType.SUBS) {
                    val offerToken = productDetails.subscriptionOfferDetails?.get(0)?.offerToken ?: ""
                    listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(productDetails)
                            .setOfferToken(offerToken)
                            .build()
                    )
                } else {
                    listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(productDetails)
                            .build()
                    )
                }

                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()

                billingClient.launchBillingFlow(activity, billingFlowParams)
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        _isPremium.value = true
                    }
                }
            } else {
                _isPremium.value = true
            }
        }
    }
}

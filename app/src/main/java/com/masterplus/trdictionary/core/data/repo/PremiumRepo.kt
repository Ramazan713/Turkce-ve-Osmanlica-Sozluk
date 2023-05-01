package com.masterplus.trdictionary.core.data.repo

import android.content.Context
import com.android.billingclient.api.*
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.util.UiText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PremiumRepo @Inject constructor(
    context: Context,
    private val scope: CoroutineScope
) {

    private var mutableProducts = MutableSharedFlow<List<ProductDetails>>(replay = 1)
    val products: SharedFlow<List<ProductDetails>> get() = mutableProducts

    private var mutablePremiumActive = MutableSharedFlow<Boolean>(1)
    val premiumActive: SharedFlow<Boolean> get() = mutablePremiumActive

    private var mutableMessages = MutableSharedFlow<UiText>(replay = 1)
    val messages: SharedFlow<UiText> get() = mutableMessages

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    scope.launch {
                        handlePurchase(purchase)
                    }
                }
            }
        }

    var billingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()
        private set


    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if(billingResult.responseCode ==  BillingClient.BillingResponseCode.OK){
                    scope.launch {
                        checkPremium()
                        getProducts()
                    }
                }
            }
            override fun onBillingServiceDisconnected() {}
        })
    }

    private suspend fun getProducts(){

        val productList = ArrayList<QueryProductDetailsParams.Product>()
        productList.add(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("tr_osm_dict_basic_premium")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )
        val params = QueryProductDetailsParams.newBuilder()
        params.setProductList(productList)

        val productDetailsResult = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(params.build())
        }
        mutableProducts.emit(productDetailsResult.productDetailsList?.toList() ?: emptyList())
    }


    fun getBillingFlowParams(productDetails: ProductDetails,offerToken: String): BillingFlowParams{

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(offerToken)
                .build()
        )

        return BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
    }

    private suspend fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            mutablePremiumActive.emit(true)
            if (!purchase.isAcknowledged) {
                acknowledgedPurchase(purchase)
            }
            mutableMessages.emit(UiText.Resource(R.string.success_purchase))
        }
    }

    suspend fun checkPremium(){

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)

        val purchasesResult = billingClient.queryPurchasesAsync(params.build())
        val hasPurchased = purchasesResult.purchasesList.any { purchase->
            if(purchase.purchaseState == Purchase.PurchaseState.PURCHASED){
                if(!purchase.isAcknowledged) acknowledgedPurchase(purchase)
                return@any true
            }
            false
        }
        mutablePremiumActive.emit(hasPurchased)
    }

    private suspend fun acknowledgedPurchase(purchase: Purchase){
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
        withContext(Dispatchers.IO) {
            billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
        }
    }

}




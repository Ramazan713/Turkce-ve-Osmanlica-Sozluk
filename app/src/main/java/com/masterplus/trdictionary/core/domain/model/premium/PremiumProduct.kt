package com.masterplus.trdictionary.core.domain.model.premium

import com.android.billingclient.api.ProductDetails
import com.masterplus.trdictionary.core.data.local.mapper.toSubsOffer


data class PremiumProduct(
    val product: ProductDetails?
){
    val name: String? get() = product?.name

    val subProducts: List<SubsProduct> get() = product?.subscriptionOfferDetails?.groupBy { it.basePlanId }?.mapNotNull { items ->
        val basePlanId = items.key
        if(items.value.isEmpty()) return@mapNotNull null

        val freeSubs = items.value.firstOrNull { offerDetails ->
            offerDetails.pricingPhases.pricingPhaseList.any { it.priceAmountMicros == 0L }
        }?.toSubsOffer()

        val mainSubs = items.value.firstOrNull { offerDetails ->
            offerDetails.pricingPhases.pricingPhaseList.all { it.priceAmountMicros != 0L }
        }?.toSubsOffer() ?: return@mapNotNull null

        SubsProduct(
            basePlanId = basePlanId,
            mainSubsOffer = mainSubs,
            freeTrialSubsOffer = freeSubs
        )
    }?: emptyList()
}
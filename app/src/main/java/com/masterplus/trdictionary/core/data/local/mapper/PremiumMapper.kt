package com.masterplus.trdictionary.core.data.local.mapper

import com.android.billingclient.api.ProductDetails
import com.masterplus.trdictionary.core.domain.model.premium.BillingPeriod
import com.masterplus.trdictionary.core.domain.model.premium.PriceInfo
import com.masterplus.trdictionary.core.domain.model.premium.SubsOffer


fun ProductDetails.PricingPhase.toPriceInfo(): PriceInfo?{
    return PriceInfo(
        priceLabel = formattedPrice,
        period = BillingPeriod.fromBillingPeriod(billingPeriod) ?: return null,
        free = priceAmountMicros == 0L
    )
}

fun ProductDetails.SubscriptionOfferDetails.toSubsOffer(): SubsOffer {
    return SubsOffer(
        offerToken = offerToken,
        priceInfos = pricingPhases.pricingPhaseList.mapNotNull { it.toPriceInfo() }
    )
}
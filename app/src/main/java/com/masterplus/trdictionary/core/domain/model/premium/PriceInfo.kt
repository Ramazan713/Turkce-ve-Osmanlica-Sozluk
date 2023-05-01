package com.masterplus.trdictionary.core.domain.model.premium


data class PriceInfo(
    val priceLabel: String,
    val period: BillingPeriod,
    val free: Boolean
)

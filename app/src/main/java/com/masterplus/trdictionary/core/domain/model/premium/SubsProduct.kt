package com.masterplus.trdictionary.core.domain.model.premium

data class SubsProduct(
    val basePlanId: String,
    val mainSubsOffer: SubsOffer,
    val freeTrialSubsOffer: SubsOffer?
)

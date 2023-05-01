package com.masterplus.trdictionary.core.domain.model.premium


data class SubsOffer(
    val offerToken: String,
    val priceInfos: List<PriceInfo>
){
    val firstNonFree = priceInfos.firstOrNull { !it.free }
    val free = priceInfos.firstOrNull { it.free }
}
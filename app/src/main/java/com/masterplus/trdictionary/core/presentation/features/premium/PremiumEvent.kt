package com.masterplus.trdictionary.core.presentation.features.premium

import com.masterplus.trdictionary.core.domain.model.premium.PremiumProduct

sealed interface PremiumEvent{

    object ClearMessage: PremiumEvent

    object ClearUiEvent: PremiumEvent

    data class Purchase(val premiumProduct: PremiumProduct, val offerToken: String): PremiumEvent

    object CheckPremium: PremiumEvent
}

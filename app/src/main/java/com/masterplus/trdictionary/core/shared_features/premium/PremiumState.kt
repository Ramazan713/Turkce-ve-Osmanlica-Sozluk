package com.masterplus.trdictionary.core.shared_features.premium

import com.masterplus.trdictionary.core.util.UiText

data class PremiumState(
    val isPremium: Boolean = false,
    val message: UiText? = null,
    val uiEvent: PremiumUiEvent? = null
)

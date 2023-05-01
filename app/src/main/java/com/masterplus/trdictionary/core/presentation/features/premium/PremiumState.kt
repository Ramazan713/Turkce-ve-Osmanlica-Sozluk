package com.masterplus.trdictionary.core.presentation.features.premium

import com.masterplus.trdictionary.core.domain.util.UiText

data class PremiumState(
    val isPremium: Boolean = false,
    val message: UiText? = null,
    val uiEvent: PremiumUiEvent? = null
)

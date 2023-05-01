package com.masterplus.trdictionary.features.app.presentation.in_app

data class InAppFeaturesState(
    val enabledReviewApi: Boolean = false,
    val reviewApiDestCount: Int = 0,
    val uiEvent: InAppUiEvent? = null
)

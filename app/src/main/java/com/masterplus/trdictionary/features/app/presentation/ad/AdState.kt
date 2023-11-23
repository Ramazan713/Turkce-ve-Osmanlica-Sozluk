package com.masterplus.trdictionary.features.app.presentation.ad

data class AdState(
    val openingCount: Int = 0,
    val consumeSeconds: Int = 0,
    val currentDestination: String? = null
)
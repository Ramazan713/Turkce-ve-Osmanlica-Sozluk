package com.masterplus.trdictionary.features.app.presentation.ad


sealed class AdEvent{

    data class CheckFromDestination(val routeId: String?): AdEvent()

    object Reset: AdEvent()

    data class SetPremiumActive(val premiumActive: Boolean): AdEvent()

    object ClearUiEvent: AdEvent()
}
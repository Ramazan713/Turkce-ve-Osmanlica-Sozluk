package com.masterplus.trdictionary.features.app.presentation.in_app

sealed class InAppEvent{

    data class CheckDestination(val routeId: String?): InAppEvent()

    object ClearUiEvent: InAppEvent()
}

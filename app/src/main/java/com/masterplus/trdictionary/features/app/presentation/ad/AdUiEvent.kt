package com.masterplus.trdictionary.features.app.presentation.ad

sealed class AdUiEvent{
    object LoadAd: AdUiEvent()

    object CheckAdShowState: AdUiEvent()
}

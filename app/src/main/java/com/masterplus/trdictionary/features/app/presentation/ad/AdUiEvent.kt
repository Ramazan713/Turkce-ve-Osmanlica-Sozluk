package com.masterplus.trdictionary.features.app.presentation.ad

sealed class AdUiEvent{
    data object LoadAd: AdUiEvent()

    data object CheckAdShowState: AdUiEvent()
}

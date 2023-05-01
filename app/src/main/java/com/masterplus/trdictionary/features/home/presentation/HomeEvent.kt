package com.masterplus.trdictionary.features.home.presentation

sealed class HomeEvent{
    data class RefreshShortInfo(val shortInfoModel: ShortInfoModel): HomeEvent()
}

package com.masterplus.trdictionary.features.home.presentation

import com.masterplus.trdictionary.features.home.domain.models.ShortInfoModel

sealed class HomeEvent{
    data class RefreshShortInfo(val shortInfoModel: ShortInfoModel): HomeEvent()

    data object LoadData: HomeEvent()
}

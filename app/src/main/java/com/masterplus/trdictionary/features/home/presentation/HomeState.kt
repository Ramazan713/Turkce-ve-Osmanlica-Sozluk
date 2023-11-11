package com.masterplus.trdictionary.features.home.presentation

import com.masterplus.trdictionary.features.home.domain.models.ShortInfoModel


data class HomeState(
    val wordShortInfo: ShortInfoModel = ShortInfoModel(),
    val proverbShortInfo: ShortInfoModel = ShortInfoModel(),
    val idiomShortInfo: ShortInfoModel = ShortInfoModel(),
)

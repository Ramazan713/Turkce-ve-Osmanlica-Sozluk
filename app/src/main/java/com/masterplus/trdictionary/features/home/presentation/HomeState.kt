package com.masterplus.trdictionary.features.home.presentation


data class HomeState(
    val wordShortInfo: ShortInfoModel = ShortInfoModel(),
    val proverbShortInfo: ShortInfoModel = ShortInfoModel(),
    val idiomShortInfo: ShortInfoModel = ShortInfoModel(),
)

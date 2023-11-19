package com.masterplus.trdictionary.features.home.presentation

import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoModel


data class HomeState(
    val wordShortInfo: ShortInfoModel = ShortInfoModel(shortInfo = ShortInfoEnum.Word, isLoading = true),
    val proverbShortInfo: ShortInfoModel = ShortInfoModel(shortInfo = ShortInfoEnum.Proverb, isLoading = true),
    val idiomShortInfo: ShortInfoModel = ShortInfoModel(shortInfo = ShortInfoEnum.Idiom, isLoading = true),
)

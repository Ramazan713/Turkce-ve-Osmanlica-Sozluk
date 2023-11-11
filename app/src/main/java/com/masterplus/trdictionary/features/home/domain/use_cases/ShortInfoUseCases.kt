package com.masterplus.trdictionary.features.home.domain.use_cases

import com.masterplus.trdictionary.features.home.domain.use_cases.widget.LoadShortInfoWidget
import com.masterplus.trdictionary.features.home.domain.use_cases.widget.SendShortInfoToWidget

data class ShortInfoUseCases(
    val getShortInfo: GetShortInfo,
    val getShortInfoResult: GetShortInfosResult
)

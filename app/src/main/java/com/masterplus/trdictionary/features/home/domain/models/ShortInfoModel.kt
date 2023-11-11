package com.masterplus.trdictionary.features.home.domain.models

import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum


data class ShortInfoModel(
    val simpleWord: SimpleWordResult? = null,
    val isLoading: Boolean = false,
    val shortInfo: ShortInfoEnum = ShortInfoEnum.Word
)

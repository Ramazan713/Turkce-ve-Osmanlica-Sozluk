package com.masterplus.trdictionary.features.home.domain.models

import com.masterplus.trdictionary.core.domain.model.SimpleWordResult

data class ShortInfoCollectionResult(
    val word: SimpleWordResult?,
    val idiom: SimpleWordResult?,
    val proverb: SimpleWordResult?,
)
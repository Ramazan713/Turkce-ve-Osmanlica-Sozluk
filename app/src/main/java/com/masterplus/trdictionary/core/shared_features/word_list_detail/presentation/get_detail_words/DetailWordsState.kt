package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.get_detail_words

import com.masterplus.trdictionary.core.domain.model.SimpleWordResult

data class DetailWordsState(
    val proverbIdiomWords: List<SimpleWordResult> = emptyList(),
    val compoundWords: List<SimpleWordResult> = emptyList()
)

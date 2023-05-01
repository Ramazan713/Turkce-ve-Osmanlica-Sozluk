package com.masterplus.trdictionary.features.word_detail.domain.model

import com.masterplus.trdictionary.core.domain.model.Word

data class WordMeanings(
    val word: Word,
    val meanings: List<MeaningExamples>
)

package com.masterplus.trdictionary.features.word_detail.domain.model

import com.masterplus.trdictionary.core.domain.model.Word

data class WordListInfoSimilarWords(
    val wordListInfo: WordListInfo,
    val similarWords: List<Word>
)

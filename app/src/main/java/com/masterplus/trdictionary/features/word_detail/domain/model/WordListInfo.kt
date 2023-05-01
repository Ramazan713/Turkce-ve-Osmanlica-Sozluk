package com.masterplus.trdictionary.features.word_detail.domain.model

data class WordListInfo(
    val wordMeaning: WordMeanings,
    val inAnyList: Boolean,
    val inFavorite: Boolean
)

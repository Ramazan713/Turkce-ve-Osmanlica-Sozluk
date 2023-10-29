package com.masterplus.trdictionary.features.word_detail.domain.model

data class WordWithSimilarRelationModel(
        val wordDetailMeanings: WordDetailMeanings,
        val similarWords: List<WordDetail>
)

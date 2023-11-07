package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model

data class WordWithSimilarRelationModel(
    val wordDetailMeanings: WordDetailMeanings,
    val similarWords: List<WordDetail>
)

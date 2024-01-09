package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.data.local.entities.relations.MeaningExamplesRelation
import com.masterplus.trdictionary.core.data.local.entities.relations.WordMeaningsRelation
import com.masterplus.trdictionary.core.data.local.views.WordDetailView
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.MeaningExamples
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetail
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings

fun wordDetailMeanings(
    wordId: Int = 1,
    wordType: WordType = WordType.PureWord,
    wordDetail: WordDetail = wordDetail(id = wordId, wordType = wordType),
    meanings: List<MeaningExamples> = listOf(
        meaningExamples(meaningId = 1, wordId = wordDetail.id),
        meaningExamples(meaningId = 2, wordId = wordDetail.id)
    )
): WordDetailMeanings {
    return WordDetailMeanings(
        wordDetail, meanings
    )
}


fun wordDetailMeaningsRelation(
    wordId: Int = 1,
    wordType: WordType = WordType.PureWord,
    wordDetail: WordDetailView = wordDetailView(id = wordId, wordType = wordType),
    meanings: List<MeaningExamplesRelation> = listOf(
        meaningExamplesRelation(meaningId = 1, wordId = wordDetail.id),
        meaningExamplesRelation(meaningId = 2, wordId = wordDetail.id)
    )
): WordMeaningsRelation {
    return WordMeaningsRelation(
        wordDetail, meanings
    )
}
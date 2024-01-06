package com.masterplus.trdictionary.core.utils.sample_data

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
package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.entities.relations.WordMeaningsRelation
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings

fun WordMeaningsRelation.toWordMeanings(): WordDetailMeanings {
    return WordDetailMeanings(
        wordDetail = word.toWordDetail(),
        meanings = meanings.map { it.toMeaningExamples() }
    )
}
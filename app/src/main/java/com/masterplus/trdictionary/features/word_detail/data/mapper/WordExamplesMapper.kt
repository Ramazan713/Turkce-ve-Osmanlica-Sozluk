package com.masterplus.trdictionary.features.word_detail.data.mapper

import com.masterplus.trdictionary.core.data.local.entities.relations.WordMeaningsRelation
import com.masterplus.trdictionary.core.data.local.mapper.toWordDetail
import com.masterplus.trdictionary.features.word_detail.domain.model.WordDetailMeanings

fun WordMeaningsRelation.toWordMeanings(): WordDetailMeanings{
    return WordDetailMeanings(
        wordDetail = word.toWordDetail(),
        meanings = meanings.map { it.toMeaningExamples() }
    )
}
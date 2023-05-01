package com.masterplus.trdictionary.features.word_detail.data.mapper

import com.masterplus.trdictionary.core.data.local.mapper.toWord
import com.masterplus.trdictionary.core.data.local.entities.relations.WordMeaningsRelation
import com.masterplus.trdictionary.features.word_detail.domain.model.WordMeanings

fun WordMeaningsRelation.toWordMeanings(): WordMeanings{
    return WordMeanings(
        word = word.toWord(),
        meanings = meanings.map { it.toMeaningExamples() }
    )
}
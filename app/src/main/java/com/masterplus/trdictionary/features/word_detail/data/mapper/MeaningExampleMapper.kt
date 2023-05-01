package com.masterplus.trdictionary.features.word_detail.data.mapper

import com.masterplus.trdictionary.core.data.local.mapper.toMeaning
import com.masterplus.trdictionary.core.data.local.entities.relations.MeaningExamplesRelation
import com.masterplus.trdictionary.features.word_detail.domain.model.MeaningExamples

fun MeaningExamplesRelation.toMeaningExamples(): MeaningExamples {
    return MeaningExamples(
        meaning = meaning.toMeaning(),
        examples = examples.map { it.toExampleAuthor() }
    )
}
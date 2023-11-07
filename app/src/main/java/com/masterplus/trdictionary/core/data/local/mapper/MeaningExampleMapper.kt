package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.entities.relations.MeaningExamplesRelation
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.MeaningExamples

fun MeaningExamplesRelation.toMeaningExamples(): MeaningExamples {
    return MeaningExamples(
        meaning = meaning.toMeaning(),
        examples = examples.map { it.toExampleDetail() }
    )
}
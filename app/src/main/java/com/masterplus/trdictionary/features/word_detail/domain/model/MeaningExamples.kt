package com.masterplus.trdictionary.features.word_detail.domain.model

import com.masterplus.trdictionary.core.domain.model.Meaning

data class MeaningExamples(
    val meaning: Meaning,
    val examples: List<ExampleAuthor>
)

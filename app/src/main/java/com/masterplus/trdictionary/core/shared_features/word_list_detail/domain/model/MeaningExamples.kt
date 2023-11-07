package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model

import com.masterplus.trdictionary.core.domain.model.Meaning

data class MeaningExamples(
    val meaning: Meaning,
    val examples: List<ExampleDetail>
)

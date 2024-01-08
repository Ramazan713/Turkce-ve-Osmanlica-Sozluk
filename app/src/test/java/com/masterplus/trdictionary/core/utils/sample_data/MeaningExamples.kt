package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.data.local.entities.MeaningEntity
import com.masterplus.trdictionary.core.data.local.entities.relations.MeaningExamplesRelation
import com.masterplus.trdictionary.core.data.local.views.ExampleDetailsView
import com.masterplus.trdictionary.core.domain.model.Meaning
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.ExampleDetail
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.MeaningExamples


fun meaningExamples(
    meaningId: Int = 1,
    wordId: Int = 1,
    meaning: Meaning = meaning(id = meaningId, wordId = wordId),
    examples: List<ExampleDetail> = listOf(
        exampleDetail(id = 1, meaningId = meaning.id!!),
        exampleDetail(id = 2, meaningId = meaning.id!!),
    )
): MeaningExamples {
   return MeaningExamples(
       meaning, examples
   )
}


fun meaningExamplesRelation(
    meaningId: Int = 1,
    wordId: Int = 1,
    meaning: MeaningEntity = meaningEntity(id = meaningId, wordId = wordId),
    examples: List<ExampleDetailsView> = listOf(
        exampleDetailView(id = 1, meaningId = meaning.id),
        exampleDetailView(id = 2, meaningId = meaning.id),
    )
): MeaningExamplesRelation {
    return MeaningExamplesRelation(
        meaning, examples
    )
}
package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.entities.relations.WordWithSimilarRelation
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilarRelationModel

fun WordWithSimilarRelation.toWordWithSimilar(): WordWithSimilarRelationModel {
    return WordWithSimilarRelationModel(
        wordDetailMeanings = wordInfo.toWordMeanings(),
        similarWords = similarityWords.map { it.toWordDetail() }
    )
}
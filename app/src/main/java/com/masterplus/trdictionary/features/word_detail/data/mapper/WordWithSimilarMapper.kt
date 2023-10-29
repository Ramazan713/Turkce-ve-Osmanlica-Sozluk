package com.masterplus.trdictionary.features.word_detail.data.mapper

import com.masterplus.trdictionary.core.data.local.entities.relations.WordWithSimilarRelation
import com.masterplus.trdictionary.core.data.local.mapper.toWordDetail
import com.masterplus.trdictionary.features.word_detail.domain.model.WordWithSimilarRelationModel

fun WordWithSimilarRelation.toWordWithSimilar(): WordWithSimilarRelationModel{
    return WordWithSimilarRelationModel(
        wordDetailMeanings = wordInfo.toWordMeanings(),
        similarWords = similarityWords.map { it.toWordDetail() }
    )
}
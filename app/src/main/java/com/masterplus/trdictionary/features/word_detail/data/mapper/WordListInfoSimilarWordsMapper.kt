package com.masterplus.trdictionary.features.word_detail.data.mapper

import com.masterplus.trdictionary.core.data.local.mapper.toWord
import com.masterplus.trdictionary.core.data.local.entities.relations.WordListInfoSimilaritiesRelation
import com.masterplus.trdictionary.features.word_detail.domain.model.WordListInfoSimilarWords

fun WordListInfoSimilaritiesRelation.toWordListInfoSimilarWords(): WordListInfoSimilarWords {
    return WordListInfoSimilarWords(
        wordListInfo = wordInfo.toWordListInfo(),
        similarWords = similarityWords.map { it.toWord() }
    )
}
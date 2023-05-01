package com.masterplus.trdictionary.features.word_detail.data.mapper

import com.masterplus.trdictionary.core.data.local.entities.relations.WordListInfoRelation
import com.masterplus.trdictionary.features.word_detail.domain.model.WordListInfo

fun WordListInfoRelation.toWordListInfo(): WordListInfo {
    return WordListInfo(
        wordMeaning = wordMeaningsRelation.toWordMeanings(),
        inAnyList = wordListsViewEntity.inAnyList,
        inFavorite = wordListsViewEntity.inFavorite
    )
}
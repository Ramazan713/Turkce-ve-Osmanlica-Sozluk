package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.entities.ListWordsEntity
import com.masterplus.trdictionary.core.domain.model.ListWords

fun ListWordsEntity.toListWords(): ListWords {
    return ListWords(
        listId = listId,
        wordId = wordId,
        pos = pos
    )
}

fun ListWords.toListWordsEntity(): ListWordsEntity {
    return ListWordsEntity(
        listId = listId,
        wordId = wordId,
        pos = pos
    )
}

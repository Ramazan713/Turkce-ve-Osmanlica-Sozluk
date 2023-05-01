package com.masterplus.trdictionary.features.search.data.mapper

import com.masterplus.trdictionary.core.data.local.entities.HistoryEntity
import com.masterplus.trdictionary.features.search.domain.model.History


fun HistoryEntity.toHistory(): History {
    return History(
        id, content, wordId, timeStamp
    )
}

fun History.toHistoryEntity(): HistoryEntity{
    return HistoryEntity(
        id, content, wordId, timeStamp
    )
}
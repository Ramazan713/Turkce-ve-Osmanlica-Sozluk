package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.entities.relations.SimpleWordResultRelation
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult

fun SimpleWordResultRelation.toSimpleResult(): SimpleWordResult {
    return SimpleWordResult(
        word = wordEntity.toWord(),
        meanings = meanings.map { it.toMeaning() },
    )
}
package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.entities.MeaningEntity
import com.masterplus.trdictionary.core.domain.model.Meaning

fun MeaningEntity.toMeaning(): Meaning {
    return Meaning(
        id = id,
        wordId = wordId,
        orderItem = orderItem,
        meaning = meaning,
        feature = feature
    )
}
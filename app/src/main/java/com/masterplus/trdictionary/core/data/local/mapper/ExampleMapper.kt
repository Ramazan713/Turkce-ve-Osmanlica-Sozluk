package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.entities.ExampleEntity
import com.masterplus.trdictionary.core.domain.model.Example

fun ExampleEntity.toExample(): Example{
    return Example(
        id = id,
        meaningId = meaningId,
        authorId = authorId,
        orderItem = orderItem,
        content = content
    )
}
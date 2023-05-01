package com.masterplus.trdictionary.core.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.masterplus.trdictionary.core.data.local.entities.AuthorEntity
import com.masterplus.trdictionary.core.data.local.entities.ExampleEntity

data class ExampleAuthorRelation(
    @Embedded
    val example: ExampleEntity,
    @Relation(
        entity = AuthorEntity::class,
        parentColumn = "authorId",
        entityColumn = "id"
    )
    val author: AuthorEntity
)

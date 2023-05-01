package com.masterplus.trdictionary.core.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.masterplus.trdictionary.core.data.local.entities.ExampleEntity
import com.masterplus.trdictionary.core.data.local.entities.MeaningEntity

data class MeaningExamplesRelation(
    @Embedded
    val meaning: MeaningEntity,

    @Relation(
        entity = ExampleEntity::class,
        parentColumn = "id",
        entityColumn = "meaningId"
    )
    val examples: List<ExampleAuthorRelation>
)

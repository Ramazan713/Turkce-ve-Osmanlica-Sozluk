package com.masterplus.trdictionary.core.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.masterplus.trdictionary.core.data.local.entities.ExampleEntity
import com.masterplus.trdictionary.core.data.local.entities.MeaningEntity
import com.masterplus.trdictionary.core.data.local.views.ExampleDetailsView

data class MeaningExamplesRelation(
    @Embedded
    val meaning: MeaningEntity,

    @Relation(
        entity = ExampleDetailsView::class,
        parentColumn = "id",
        entityColumn = "meaningId"
    )
    val examples: List<ExampleDetailsView>
)

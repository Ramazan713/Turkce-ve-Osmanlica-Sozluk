package com.masterplus.trdictionary.core.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.masterplus.trdictionary.core.data.local.entities.MeaningEntity
import com.masterplus.trdictionary.core.data.local.views.WordDetailView

data class WordMeaningsRelation(
    @Embedded
    val word: WordDetailView,

    @Relation(
        entity = MeaningEntity::class,
        parentColumn = "id",
        entityColumn = "wordId"
    )
    val meanings: List<MeaningExamplesRelation>
)

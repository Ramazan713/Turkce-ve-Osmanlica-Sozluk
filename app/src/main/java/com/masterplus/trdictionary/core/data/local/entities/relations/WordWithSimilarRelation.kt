package com.masterplus.trdictionary.core.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.masterplus.trdictionary.core.data.local.entities.SimilarWordsCrossRef
import com.masterplus.trdictionary.core.data.local.views.WordDetailView

data class WordWithSimilarRelation(
    @Embedded
    val wordInfo: WordMeaningsRelation,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            SimilarWordsCrossRef::class,
            parentColumn = "wordId",
            entityColumn = "similarWordId"
        )
    )
    val similarityWords: List<WordDetailView>,
)

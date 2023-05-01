package com.masterplus.trdictionary.core.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.masterplus.trdictionary.core.data.local.entities.SimilarWordsCrossRef
import com.masterplus.trdictionary.core.data.local.entities.WordEntity

data class WordListInfoSimilaritiesRelation(
    @Embedded
    val wordInfo: WordListInfoRelation,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            SimilarWordsCrossRef::class,
            parentColumn = "wordId",
            entityColumn = "similarWordId"
        )
    )
    val similarityWords: List<WordEntity>,
)
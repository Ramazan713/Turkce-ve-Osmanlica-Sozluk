package com.masterplus.trdictionary.core.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.masterplus.trdictionary.core.data.local.views.WordListsViewEntity

data class WordListInfoRelation(
    @Embedded
    val wordMeaningsRelation: WordMeaningsRelation,
    @Relation(
        entity = WordListsViewEntity::class,
        parentColumn = "id",
        entityColumn = "wordId"
    )
    val wordListsViewEntity: WordListsViewEntity,
)

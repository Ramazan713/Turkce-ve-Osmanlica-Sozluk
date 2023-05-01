package com.masterplus.trdictionary.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "ListWords",
    foreignKeys = [
        ForeignKey(
            entity = WordEntity::class,
            parentColumns = ["id"],
            childColumns = ["wordId"]
        ),
        ForeignKey(
            entity = ListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"]
        )
    ],
    primaryKeys = ["listId","wordId"],
    indices = [
        Index("wordId")
    ]
)
data class ListWordsEntity(
    val listId: Int,
    val wordId: Int,
    val pos: Int
)

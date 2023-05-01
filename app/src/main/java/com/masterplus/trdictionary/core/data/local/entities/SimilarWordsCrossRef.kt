package com.masterplus.trdictionary.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "SimilarWords",
    foreignKeys = [
        ForeignKey(
            entity = WordEntity::class,
            parentColumns = ["id"],
            childColumns = ["wordId"]
        ),
        ForeignKey(
            entity = WordEntity::class,
            parentColumns = ["id"],
            childColumns = ["similarWordId"]
        ),
    ],
    primaryKeys = ["wordId","similarWordId"],
    indices = [
        Index("similarWordId")
    ]
)
data class SimilarWordsCrossRef(
    val wordId: Int,

    val similarWordId: Int,
)


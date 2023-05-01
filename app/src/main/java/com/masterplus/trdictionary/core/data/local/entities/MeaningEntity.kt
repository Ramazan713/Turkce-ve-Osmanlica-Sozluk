package com.masterplus.trdictionary.core.data.local.entities

import androidx.room.*

@Entity(
    tableName = "Meanings",
    foreignKeys = [
        ForeignKey(
            entity = WordEntity::class,
            parentColumns = ["id"],
            childColumns = ["wordId"]
        )
    ],
    indices = [
        Index("wordId")
    ]
)
data class MeaningEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    val wordId: Int,

    @ColumnInfo(defaultValue = "0")
    val orderItem: Int,

    val meaning: String,

    val feature: String?
)

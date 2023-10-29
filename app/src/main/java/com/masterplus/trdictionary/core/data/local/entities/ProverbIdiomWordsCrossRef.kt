package com.masterplus.trdictionary.core.data.local.entities

import androidx.room.*


@Entity(
    tableName = "ProverbIdiomWords",
    foreignKeys = [
        ForeignKey(
            entity = WordEntity::class,
            parentColumns = ["id"],
            childColumns = ["wordId"]
        ),
    ],
    indices = [
        Index("wordId")
    ]
)
data class ProverbIdiomWordsCrossRef(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,

    val groupId: Int,

    val wordId: Int,

    @ColumnInfo(defaultValue = "0")
    val orderItem: Int
)

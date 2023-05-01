package com.masterplus.trdictionary.core.data.local.entities

import androidx.room.*

@Entity(
    tableName = "Examples",
    foreignKeys = [
        ForeignKey(
            entity = MeaningEntity::class,
            parentColumns = ["id"],
            childColumns = ["meaningId"]
        ),
        ForeignKey(
            entity = AuthorEntity::class,
            parentColumns = ["id"],
            childColumns = ["authorId"]
        )
    ],
    indices = [
        Index("meaningId"),
        Index("authorId")
    ]
)
data class ExampleEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    val meaningId: Int,

    val authorId: Int,

    @ColumnInfo(defaultValue = "0")
    val orderItem: Int,

    val content: String
)

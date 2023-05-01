package com.masterplus.trdictionary.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Fts4(contentEntity = WordEntity::class)
@Entity(tableName = "WordsFts")
data class WordFtsEntity(
    @PrimaryKey
    @ColumnInfo("rowid")
    val rowId: Int,

    val word: String,

    val searchWord: String,
)

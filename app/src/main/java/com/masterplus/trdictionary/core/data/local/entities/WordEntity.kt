package com.masterplus.trdictionary.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Words")
data class WordEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    val prefix: String?,

    val word: String,

    val suffix: String?,

    val searchWord: String,

    @ColumnInfo(defaultValue = "1")
    val showInQuery: Int,

    @ColumnInfo(defaultValue = "0")
    val randomOrder: Int,

    val dictTypeId: Int,

    @ColumnInfo(defaultValue = "1")
    val wordTypeId: Int,

    @ColumnInfo(defaultValue = "1")
    val showTTS: Boolean
)

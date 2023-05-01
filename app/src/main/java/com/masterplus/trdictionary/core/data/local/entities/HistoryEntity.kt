package com.masterplus.trdictionary.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Histories")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val content: String,
    val wordId: Int,
    val timeStamp: Long
)

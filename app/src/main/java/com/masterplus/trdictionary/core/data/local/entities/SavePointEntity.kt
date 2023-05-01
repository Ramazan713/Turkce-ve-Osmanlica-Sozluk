package com.masterplus.trdictionary.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavePoints")
data class SavePointEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String,
    val itemPosIndex: Int,
    val typeId: Int,
    val destinationId: Int,
    val saveKey: String,
    val modifiedTimestamp: Long,
    val autoType: Int
)

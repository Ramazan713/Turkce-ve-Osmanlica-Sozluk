package com.masterplus.trdictionary.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BackupMetas")
data class BackupMetaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val fileName: String,
    val updatedDate: Long,
)

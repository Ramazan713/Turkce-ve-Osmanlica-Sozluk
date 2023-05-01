package com.masterplus.trdictionary.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Lists")
data class ListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    @ColumnInfo(defaultValue = "1")
    val isRemovable: Boolean = true,
    @ColumnInfo(defaultValue = "0")
    val isArchive: Boolean = false,
    val pos: Int
)

package com.masterplus.trdictionary.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Authors")
data class AuthorEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val shortName: String
)

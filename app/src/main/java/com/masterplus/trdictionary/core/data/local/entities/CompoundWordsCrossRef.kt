package com.masterplus.trdictionary.core.data.local.entities

import androidx.room.Entity

@Entity(
    tableName = "CompoundWords",
    primaryKeys = ["wordId","compoundWordId"]
)
data class CompoundWordsCrossRef(
    val wordId: Int,

    val compoundWordId: Int
)

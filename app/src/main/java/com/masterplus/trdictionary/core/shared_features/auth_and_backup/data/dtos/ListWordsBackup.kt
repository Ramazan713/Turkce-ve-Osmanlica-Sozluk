package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ListWordsBackup(
    val listId: Int,
    val wordId: Int,
    val pos: Int
)

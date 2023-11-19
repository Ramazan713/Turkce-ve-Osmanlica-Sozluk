package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryBackup(
    val id: Int?,
    val content: String,
    val timeStamp: Long
)

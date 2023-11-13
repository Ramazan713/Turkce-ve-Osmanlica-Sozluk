package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model

data class HistoryBackup(
    val id: Int?,
    val content: String,
    val timeStamp: Long
)

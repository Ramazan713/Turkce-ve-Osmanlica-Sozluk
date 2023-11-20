package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model

data class BackupMeta(
    val id: Int?,
    val fileName: String,
    val updatedDate: Long,
    val title: String = ""
)

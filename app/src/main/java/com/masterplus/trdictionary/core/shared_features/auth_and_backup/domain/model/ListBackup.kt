package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model

data class ListBackup(
    val id: Int?,
    val name: String,
    val isRemovable: Boolean,
    val isArchive: Boolean,
    val pos: Int
)

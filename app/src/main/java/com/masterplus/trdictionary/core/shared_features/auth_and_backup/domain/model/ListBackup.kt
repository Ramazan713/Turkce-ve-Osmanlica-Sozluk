package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ListBackup(
    val id: Int?,
    val name: String,
    val isRemovable: Boolean,
    val isArchive: Boolean,
    val pos: Int
)

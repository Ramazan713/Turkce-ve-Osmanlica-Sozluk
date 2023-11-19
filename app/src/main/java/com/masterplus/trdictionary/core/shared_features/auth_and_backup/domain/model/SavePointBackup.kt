package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SavePointBackup(
    val id: Int?,
    val title: String,
    val itemPosIndex: Int,
    val typeId: Int,
    val destinationId: Int,
    val saveKey: String,
    val modifiedTimestamp: Long,
    val autoType: Int
)

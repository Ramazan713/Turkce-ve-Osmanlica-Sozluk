package com.masterplus.trdictionary.features.settings.domain.model

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

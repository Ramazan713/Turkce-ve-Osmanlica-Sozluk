package com.masterplus.trdictionary.features.settings.domain.model

data class HistoryBackup(
    val id: Int?,
    val content: String,
    val wordId: Int,
    val timeStamp: Long
)

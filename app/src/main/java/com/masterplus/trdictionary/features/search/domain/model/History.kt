package com.masterplus.trdictionary.features.search.domain.model

data class History(
    val id: Int?,
    val content: String,
    val wordId: Int,
    val timeStamp: Long
)

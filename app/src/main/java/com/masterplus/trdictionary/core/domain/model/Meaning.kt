package com.masterplus.trdictionary.core.domain.model

data class Meaning(
    val id: Int?,
    val wordId: Int,
    val orderItem: Int,
    val meaning: String,
    val feature: String?
)

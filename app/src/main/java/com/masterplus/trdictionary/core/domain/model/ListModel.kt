package com.masterplus.trdictionary.core.domain.model

data class ListModel(
    val id: Int?,
    val name: String,
    val isRemovable: Boolean = true,
    val isArchive: Boolean = false,
    val pos: Int
)

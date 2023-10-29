package com.masterplus.trdictionary.features.word_detail.domain.model

data class ExampleDetail(
    val id: Int,
    val meaningId: Int,
    val authorId: Int,
    val orderItem: Int,
    val content: String,
    val authorName: String
)

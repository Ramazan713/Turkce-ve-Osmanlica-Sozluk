package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model

data class ExampleDetail(
    val id: Int,
    val meaningId: Int,
    val authorId: Int,
    val orderItem: Int,
    val content: String,
    val authorName: String
)

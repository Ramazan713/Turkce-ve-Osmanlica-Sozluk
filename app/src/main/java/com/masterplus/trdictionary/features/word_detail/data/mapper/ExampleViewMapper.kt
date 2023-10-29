package com.masterplus.trdictionary.features.word_detail.data.mapper

import com.masterplus.trdictionary.core.data.local.views.ExampleDetailsView
import com.masterplus.trdictionary.features.word_detail.domain.model.ExampleDetail

fun ExampleDetailsView.toExampleDetail(): ExampleDetail{
    return ExampleDetail(
        id, meaningId, authorId, orderItem, content, authorName
    )
}
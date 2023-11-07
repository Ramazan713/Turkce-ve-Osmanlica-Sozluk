package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.views.ExampleDetailsView
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.ExampleDetail

fun ExampleDetailsView.toExampleDetail(): ExampleDetail {
    return ExampleDetail(
        id, meaningId, authorId, orderItem, content, authorName
    )
}
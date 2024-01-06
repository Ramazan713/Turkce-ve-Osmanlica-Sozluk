package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.ExampleDetail


fun exampleDetail(
    id: Int = 1,
    meaningId: Int = 1,
    authorId: Int = 1,
    orderItem: Int = 1,
    content: String = "content $id",
    authorName: String = "Author Name $authorId"
): ExampleDetail {
   return ExampleDetail(
       id, meaningId, authorId, orderItem, content, authorName
   )
}
package com.masterplus.trdictionary.features.word_detail.domain.model

import com.masterplus.trdictionary.core.domain.model.Author
import com.masterplus.trdictionary.core.domain.model.Example

data class ExampleAuthor(
    val example: Example,
    val author: Author
)

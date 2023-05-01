package com.masterplus.trdictionary.features.word_detail.data.mapper

import com.masterplus.trdictionary.core.data.local.mapper.toAuthor
import com.masterplus.trdictionary.core.data.local.mapper.toExample
import com.masterplus.trdictionary.core.data.local.entities.relations.ExampleAuthorRelation
import com.masterplus.trdictionary.features.word_detail.domain.model.ExampleAuthor

fun ExampleAuthorRelation.toExampleAuthor(): ExampleAuthor{
    return ExampleAuthor(
        example = example.toExample(),
        author = author.toAuthor()
    )
}
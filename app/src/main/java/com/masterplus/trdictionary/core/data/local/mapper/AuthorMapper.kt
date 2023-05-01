package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.entities.AuthorEntity
import com.masterplus.trdictionary.core.domain.model.Author

fun AuthorEntity.toAuthor(): Author{
    return Author(
        id = id,
        name = name,
        shortName = shortName
    )
}
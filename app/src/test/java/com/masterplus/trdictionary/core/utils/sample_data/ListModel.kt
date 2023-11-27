package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.domain.model.ListModel

fun listModel(
    id: Int? = 1,
    name: String = "list $id",
    isRemovable: Boolean = true,
    isArchive: Boolean = false,
    pos: Int = 1
): ListModel{
    return ListModel(
        id, name, isRemovable, isArchive, pos
    )
}
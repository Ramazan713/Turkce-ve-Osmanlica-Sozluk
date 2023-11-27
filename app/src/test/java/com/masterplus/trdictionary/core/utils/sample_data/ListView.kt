package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.domain.model.ListView

fun listView(
    id: Int? = 1,
    name: String = "listView $id",
    isRemovable: Boolean = true,
    isArchive: Boolean = false,
    listPos: Int = 1,
    contentMaxPos: Int = 1,
    itemCounts: Int = 1
): ListView {
    return ListView(
        id, name, isRemovable, isArchive, listPos, contentMaxPos, itemCounts
    )
}
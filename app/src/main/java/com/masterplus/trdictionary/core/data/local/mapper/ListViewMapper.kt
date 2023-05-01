package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.views.ListViewEntity
import com.masterplus.trdictionary.core.domain.model.ListModel
import com.masterplus.trdictionary.core.domain.model.ListView

fun ListViewEntity.toListView(): ListView {
    return ListView(
        id = id,
        name = name,
        isRemovable = isRemovable,
        isArchive = isArchive,
        listPos = listPos,
        contentMaxPos = contentMaxPos,
        itemCounts = itemCounts
    )
}

fun ListView.toListModel(): ListModel {
    return ListModel(
        id = id,
        name = name,
        isRemovable = isRemovable,
        isArchive = isArchive,
        pos = listPos
    )
}






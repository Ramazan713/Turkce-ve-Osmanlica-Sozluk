package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.entities.ListEntity
import com.masterplus.trdictionary.core.domain.model.ListModel


fun ListEntity.toListModel(): ListModel {
    return ListModel(
        id = id,
        name = name,
        isArchive = isArchive,
        isRemovable = isRemovable,
        pos = pos
    )
}


fun ListModel.toListEntity(): ListEntity {
    return ListEntity(
        id = id,
        name = name,
        isArchive = isArchive,
        isRemovable = isRemovable,
        pos = pos
    )
}
package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.entities.SavePointEntity
import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.domain.enums.AutoType
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import java.util.*


fun SavePointEntity.toSavePoint(): SavePoint {
    return SavePoint(
        id = id,
        title = title,
        itemPosIndex = itemPosIndex,
        modifiedDate = Calendar.getInstance().apply { time.time = modifiedTimestamp },
        autoType = AutoType.fromTypeId(autoType),
        savePointDestination = SavePointDestination.fromDestinationId(destinationId, saveKey)
            ?: SavePointDestination.List(1)
    )
}

fun SavePoint.toSavePointEntity(): SavePointEntity {
    return SavePointEntity(
        id = id,
        title = title,
        itemPosIndex = itemPosIndex,
        modifiedTimestamp = modifiedDate.time.time,
        autoType = autoType.typeId,
        typeId = savePointDestination.type.typeId,
        destinationId = savePointDestination.destinationId,
        saveKey = savePointDestination.toSaveKey()
    )
}


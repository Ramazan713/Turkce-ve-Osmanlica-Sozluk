package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.domain.enums.AutoType
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.model.SavePoint
import java.util.Calendar

fun savePoint(
    id: Int? = 1,
    title: String = "title $id",
    itemPosIndex: Int = 1,
    savePointDestination: SavePointDestination = SavePointDestination.List(1),
    modifiedDate: Calendar = Calendar.getInstance(),
    autoType: AutoType = AutoType.Default
): SavePoint {
    return SavePoint(
        id, title, itemPosIndex, savePointDestination, modifiedDate, autoType
    )
}
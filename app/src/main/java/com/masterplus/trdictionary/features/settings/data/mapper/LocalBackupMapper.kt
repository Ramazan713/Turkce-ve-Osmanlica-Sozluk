package com.masterplus.trdictionary.features.settings.data.mapper

import com.masterplus.trdictionary.core.data.local.entities.HistoryEntity
import com.masterplus.trdictionary.features.settings.domain.model.ListBackup
import com.masterplus.trdictionary.core.data.local.entities.ListEntity
import com.masterplus.trdictionary.core.data.local.entities.ListWordsEntity
import com.masterplus.trdictionary.core.data.local.entities.SavePointEntity
import com.masterplus.trdictionary.features.settings.domain.model.HistoryBackup
import com.masterplus.trdictionary.features.settings.domain.model.ListWordsBackup
import com.masterplus.trdictionary.features.settings.domain.model.SavePointBackup


fun HistoryEntity.toHistoryBackup(): HistoryBackup {
    return HistoryBackup(
        id = id,
        content = content,
        timeStamp = timeStamp,
        wordId = wordId
    )
}

fun HistoryBackup.toHistoryEntity(): HistoryEntity{
    return HistoryEntity(
        id = id,
        content = content,
        timeStamp = timeStamp,
        wordId = wordId
    )
}



fun ListBackup.toListEntity(): ListEntity {
    return ListEntity(
        id = id,
        name = name,
        isRemovable = isRemovable,
        isArchive = isArchive,
        pos = pos
    )
}

fun ListEntity.toListBackup(): ListBackup {
    return ListBackup(
        id = id,
        name = name,
        isRemovable = isRemovable,
        isArchive = isArchive,
        pos = pos
    )
}



fun ListWordsEntity.toListWordsBackup(): ListWordsBackup {
    return ListWordsBackup(
        listId = listId,
        pos = pos,
        wordId = wordId
    )
}

fun ListWordsBackup.toListWordsEntity(): ListWordsEntity{
    return ListWordsEntity(
        listId = listId,
        wordId = wordId,
        pos = pos
    )
}




fun SavePointEntity.toSavePointBackup(): SavePointBackup {
    return SavePointBackup(
        id = id,
        title = title,
        itemPosIndex = itemPosIndex,
        saveKey = saveKey,
        modifiedTimestamp = modifiedTimestamp,
        autoType = autoType,
        destinationId = destinationId,
        typeId = typeId
    )
}

fun SavePointBackup.toSavePointEntity(): SavePointEntity{
    return SavePointEntity(
        id = id,
        title = title,
        itemPosIndex = itemPosIndex,
        saveKey = saveKey,
        modifiedTimestamp = modifiedTimestamp,
        autoType = autoType,
        destinationId = destinationId,
        typeId = typeId
    )
}
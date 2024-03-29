package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper

import com.masterplus.trdictionary.core.data.local.entities.HistoryEntity
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.dtos.ListBackup
import com.masterplus.trdictionary.core.data.local.entities.ListEntity
import com.masterplus.trdictionary.core.data.local.entities.ListWordsEntity
import com.masterplus.trdictionary.core.data.local.entities.SavePointEntity
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.dtos.HistoryBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.dtos.ListWordsBackup
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.dtos.SavePointBackup


fun HistoryEntity.toHistoryBackup(): HistoryBackup {
    return HistoryBackup(
        id = id,
        content = content,
        timeStamp = timeStamp,
    )
}

fun HistoryBackup.toHistoryEntity(): HistoryEntity{
    return HistoryEntity(
        id = id,
        content = content,
        timeStamp = timeStamp,
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
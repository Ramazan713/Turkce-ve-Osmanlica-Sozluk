package com.masterplus.trdictionary.features.settings.data.mapper

import com.google.firebase.storage.StorageMetadata
import com.masterplus.trdictionary.core.domain.enums.DateFormatEnum
import com.masterplus.trdictionary.core.util.DateFormatHelper
import com.masterplus.trdictionary.core.data.local.entities.BackupMetaEntity
import com.masterplus.trdictionary.features.settings.domain.model.BackupMeta

fun BackupMetaEntity.toBackupMeta(): BackupMeta {
    return BackupMeta(
        id = id,
        fileName = fileName,
        updatedDate = updatedDate,
        title = "Backup - ${DateFormatHelper.getReadableDate(updatedDate, DateFormatEnum.DateTimeFull)}"
    )
}

fun BackupMeta.toBackupMetaEntity(): BackupMetaEntity {
    return BackupMetaEntity(
        id = id,
        fileName = fileName,
        updatedDate = updatedDate,
    )
}

fun StorageMetadata.toBackupMeta(): BackupMeta {
    return BackupMeta(
        fileName = name?:"",
        updatedDate = updatedTimeMillis,
        id = null,
        title = "Backup - ${DateFormatHelper.getReadableDate(updatedTimeMillis, DateFormatEnum.DateTimeFull)}"
    )
}
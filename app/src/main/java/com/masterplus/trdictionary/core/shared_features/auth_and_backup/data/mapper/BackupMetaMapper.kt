package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper

import com.google.firebase.storage.StorageMetadata
import com.masterplus.trdictionary.core.domain.enums.DateFormatEnum
import com.masterplus.trdictionary.core.domain.utils.DateFormatHelper
import com.masterplus.trdictionary.core.data.local.entities.BackupMetaEntity
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.BackupMeta

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
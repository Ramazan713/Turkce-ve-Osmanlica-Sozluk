package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.BackupMeta

fun backupMeta(
    id: Int? = 1,
    fileName: String = "fileName $id",
    updatedDate: Long = 0L,
    title: String = "title $id"
): BackupMeta {
    return BackupMeta(
        id, fileName, updatedDate, title
    )
}
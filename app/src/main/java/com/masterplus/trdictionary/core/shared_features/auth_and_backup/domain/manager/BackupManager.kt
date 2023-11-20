package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager

import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.core.domain.utils.Resource

interface BackupManager {

    suspend fun uploadBackup(user: User): Resource<Unit>

    suspend fun downloadBackup(user: User, fileName: String, removeAllData: Boolean, addOnLocalData: Boolean): Resource<Unit>

    suspend fun refreshBackupMetas(user: User): Resource<Unit>

    suspend fun deleteAllLocalUserData(deleteBackupMeta: Boolean)

    suspend fun downloadLastBackup(user: User): Resource<Unit>

    suspend fun hasBackupMetas(): Boolean
}
package com.masterplus.trdictionary.features.settings.domain.manager

import com.masterplus.trdictionary.features.settings.domain.model.User
import com.masterplus.trdictionary.core.domain.util.Resource

interface BackupManager {

    suspend fun uploadBackup(user: User): Resource<Unit>

    suspend fun downloadBackup(user: User, fileName: String, removeAllData: Boolean, addOnLocalData: Boolean): Resource<Unit>

    suspend fun refreshBackupMetas(user: User): Resource<Unit>

    suspend fun deleteAllLocalUserData(deleteBackupMeta: Boolean)

    suspend fun downloadLastBackup(user: User): Resource<Unit>

    suspend fun hasBackupMetas(): Boolean
}
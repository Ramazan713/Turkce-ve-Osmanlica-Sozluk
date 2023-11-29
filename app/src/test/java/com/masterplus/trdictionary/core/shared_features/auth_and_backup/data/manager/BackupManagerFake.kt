package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.manager

import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.BackupManager
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import org.junit.jupiter.api.Assertions.*

class BackupManagerFake: BackupManager{

    var returnedUploadBackupResponse: Resource<Unit> = Resource.Success(Unit)
    var returnedDownloadBackupResponse: Resource<Unit> = Resource.Success(Unit)
    var returnedRefreshBackupMetasResponse: Resource<Unit> = Resource.Success(Unit)
    var returnedDownloadLastBackupResponse: Resource<Unit> = Resource.Success(Unit)
    var returnedHasBackupMetasResponse: Boolean = true

    override suspend fun uploadBackup(user: User): Resource<Unit> {
        return returnedUploadBackupResponse
    }

    override suspend fun downloadBackup(
        user: User,
        fileName: String,
        removeAllData: Boolean,
        addOnLocalData: Boolean
    ): Resource<Unit> {
        return returnedDownloadBackupResponse
    }

    override suspend fun refreshBackupMetas(user: User): Resource<Unit> {
        return returnedRefreshBackupMetasResponse
    }

    override suspend fun deleteAllLocalUserData(deleteBackupMeta: Boolean) {

    }

    override suspend fun downloadLastBackup(user: User): Resource<Unit> {
        return returnedDownloadLastBackupResponse
    }

    override suspend fun hasBackupMetas(): Boolean {
        return returnedHasBackupMetasResponse
    }

}
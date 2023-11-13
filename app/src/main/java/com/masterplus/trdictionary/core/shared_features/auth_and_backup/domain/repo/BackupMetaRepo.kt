package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo

import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.BackupMeta
import kotlinx.coroutines.flow.Flow

interface BackupMetaRepo {

    suspend fun insertBackupMeta(backupMeta: BackupMeta)

    suspend fun insertBackupMetas(backupMetas: List<BackupMeta>)

    suspend fun getLastBackupMeta(): BackupMeta?

    fun getBackupMetasFlow(): Flow<List<BackupMeta>>

    suspend fun deleteBackupMetas()

    suspend fun deleteBackupMetas(backupMetas: List<BackupMeta>)

    suspend fun hasBackupMetas(): Boolean

    suspend fun getExtraBackupMetas(offset: Int): List<BackupMeta>



}
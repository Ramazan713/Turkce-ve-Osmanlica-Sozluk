package com.masterplus.trdictionary.features.settings.domain.repo

import com.masterplus.trdictionary.features.settings.domain.model.BackupMeta
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
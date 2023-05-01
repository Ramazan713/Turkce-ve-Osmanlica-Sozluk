package com.masterplus.trdictionary.features.settings.data.local.repo

import com.masterplus.trdictionary.core.data.local.services.BackupMetaDao
import com.masterplus.trdictionary.features.settings.data.mapper.toBackupMeta
import com.masterplus.trdictionary.features.settings.data.mapper.toBackupMetaEntity
import com.masterplus.trdictionary.features.settings.domain.model.BackupMeta
import com.masterplus.trdictionary.features.settings.domain.repo.BackupMetaRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BackupMetaImpl @Inject constructor(
    private val backupMetaDao: BackupMetaDao
): BackupMetaRepo {
    override suspend fun insertBackupMeta(backupMeta: BackupMeta) {
        backupMetaDao.insertBackupMeta(backupMeta.toBackupMetaEntity())
    }

    override suspend fun insertBackupMetas(backupMetas: List<BackupMeta>) {
        backupMetaDao.insertBackupMetas(backupMetas.map { it.toBackupMetaEntity() })
    }

    override suspend fun getLastBackupMeta(): BackupMeta? {
        return backupMetaDao.getLastBackupMeta()?.toBackupMeta()
    }

    override fun getBackupMetasFlow(): Flow<List<BackupMeta>> {
        return backupMetaDao.getBackupMetasFlow()
            .map { items-> items.map { it.toBackupMeta() } }
    }

    override suspend fun deleteBackupMetas() {
        backupMetaDao.deleteBackupMetas()
    }

    override suspend fun deleteBackupMetas(backupMetas: List<BackupMeta>) {
        backupMetaDao.deleteBackupMetas(backupMetas.map { it.toBackupMetaEntity() })
    }

    override suspend fun hasBackupMetas(): Boolean {
        return backupMetaDao.hasBackupMetas()
    }

    override suspend fun getExtraBackupMetas(offset: Int): List<BackupMeta> {
        return backupMetaDao.getExtraBackupMetas(offset).map { it.toBackupMeta() }
    }
}
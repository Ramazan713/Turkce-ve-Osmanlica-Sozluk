package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo

import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.BackupMeta
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.BackupMetaRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BackupMetaRepoFake: BackupMetaRepo {

    private val _items = MutableStateFlow<List<BackupMeta>>(listOf())
    val items get() = _items.asStateFlow()

    override suspend fun insertBackupMeta(backupMeta: BackupMeta) {
        _items.update {
            it.toMutableList().apply {
                add(backupMeta)
            }
        }
    }

    override suspend fun insertBackupMetas(backupMetas: List<BackupMeta>) {
        _items.update {
            it.toMutableList().apply {
                addAll(backupMetas)
            }
        }
    }

    override suspend fun getLastBackupMeta(): BackupMeta? {
        return _items.value.lastOrNull()
    }

    override fun getBackupMetasFlow(): Flow<List<BackupMeta>> {
        return _items
    }

    override suspend fun deleteBackupMetas() {
       _items.update {
           listOf()
       }
    }

    override suspend fun deleteBackupMetas(backupMetas: List<BackupMeta>) {
        _items.update {
            it.toMutableList().apply {
                removeAll(backupMetas)
            }
        }
    }

    override suspend fun hasBackupMetas(): Boolean {
        return _items.value.isNotEmpty()
    }

    override suspend fun getExtraBackupMetas(offset: Int): List<BackupMeta> {
        val size = _items.value.size
        if(offset > size) return emptyList()
        return _items.value.subList(offset,_items.value.size)
    }

}
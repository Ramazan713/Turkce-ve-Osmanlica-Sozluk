package com.masterplus.trdictionary.features.settings.data.manager

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.ConnectivityProvider
import com.masterplus.trdictionary.core.domain.util.Resource
import com.masterplus.trdictionary.core.domain.util.UiText
import com.masterplus.trdictionary.features.settings.domain.model.User
import com.masterplus.trdictionary.features.settings.domain.manager.BackupManager
import com.masterplus.trdictionary.features.settings.domain.repo.StorageService
import com.masterplus.trdictionary.features.settings.domain.model.BackupMeta
import com.masterplus.trdictionary.features.settings.domain.repo.BackupMetaRepo
import com.masterplus.trdictionary.features.settings.domain.repo.LocalBackupRepo
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class BackupManagerImpl @Inject constructor(
    private val localBackupRepo: LocalBackupRepo,
    private val storageService: StorageService,
    private val backupMetaRepo: BackupMetaRepo,
    private val connectivityProvider: ConnectivityProvider
): BackupManager {

    override suspend fun uploadBackup(user: User): Resource<Unit>{
        hasInternetConnectionError<Unit>()?.let { return it }
        cutExtraBackupFiles(user)
        val fileName = UUID.randomUUID().toString()
        val timeStamp = Date().time
        val data = localBackupRepo.getJsonData().toByteArray(charset = Charsets.UTF_8)
        return storageService.uploadData(user,fileName,data).let {
            if(it is Resource.Success){
                backupMetaRepo.insertBackupMeta(
                    BackupMeta(id = null, fileName = fileName,timeStamp),
                )
            }
            it
        }
    }

    private suspend fun cutExtraBackupFiles(user: User){
        val backupMetas = backupMetaRepo.getExtraBackupMetas(K.backupMetaSizeInDb - 1)
        backupMetas.forEach { backupMeta ->
            storageService.deleteFile(user,backupMeta.fileName)
        }
        backupMetaRepo.deleteBackupMetas(backupMetas)
    }

    override suspend fun downloadBackup(user: User, fileName: String, removeAllData: Boolean, addOnLocalData: Boolean): Resource<Unit>{
        hasInternetConnectionError<Unit>()?.let { return it }
        return storageService.getFileData(user, fileName).let {
            if(it is Resource.Success){
                val dataString = it.data.toString(charset = Charsets.UTF_8)
                localBackupRepo.fromJsonData(dataString,removeAllData, addOnLocalData)
                return@let Resource.Success(Unit)
            }
            Resource.Error((it as? Resource.Error)?.error ?: UiText.Resource(R.string.error))
        }
    }

    private fun <T> hasInternetConnectionError(): Resource.Error<T>?{
        if(!connectivityProvider.hasConnection()) return Resource.Error(UiText.Resource(R.string.check_your_internet_connection))
        return null
    }

    override suspend fun refreshBackupMetas(user: User): Resource<Unit> {
        hasInternetConnectionError<Unit>()?.let { return it }
        return when(val result = storageService.getFiles(user)){
            is Resource.Success -> {
                backupMetaRepo.deleteBackupMetas()
                backupMetaRepo.insertBackupMetas(result.data)
                Resource.Success(Unit)
            }
            is Resource.Error -> {
                Resource.Error(result.error)
            }
        }
    }

    override suspend fun deleteAllLocalUserData(deleteBackupMeta: Boolean) {
        if(deleteBackupMeta){
            backupMetaRepo.deleteBackupMetas()
        }
        localBackupRepo.deleteAllLocalUserData()
    }

    override suspend fun downloadLastBackup(user: User): Resource<Unit> {
        hasInternetConnectionError<Unit>()?.let { return it }
        val backupMeta = backupMetaRepo.getLastBackupMeta()
            ?: return Resource.Error(UiText.Resource(R.string.backup_file_not_found))

        return downloadBackup(
            user = user,
            fileName = backupMeta.fileName,
            removeAllData = true,
            addOnLocalData = false
        )
    }

    override suspend fun hasBackupMetas(): Boolean {
        return backupMetaRepo.hasBackupMetas()
    }


}
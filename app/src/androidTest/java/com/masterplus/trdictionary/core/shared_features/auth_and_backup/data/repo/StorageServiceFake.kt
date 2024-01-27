package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo

import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.BackupMeta
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.StorageService
import com.masterplus.trdictionary.core.utils.sample_data.backupMeta

class StorageServiceFake: StorageService{

    var getFilesResponse: Resource<List<BackupMeta>> = Resource.Success(listOf())
    var getFileDataResponse: Resource<ByteArray> = Resource.Success(byteArrayOf())
    var deleteFileResponse: Resource<Unit> = Resource.Success(Unit)
    var uploadDataResponse: Resource<Unit> = Resource.Success(Unit)

    override suspend fun getFiles(user: User): Resource<List<BackupMeta>> {
        return getFilesResponse
    }

    override suspend fun getFileData(user: User, fileName: String): Resource<ByteArray> {
        return getFileDataResponse
    }

    override suspend fun deleteFile(user: User, fileName: String): Resource<Unit> {
        return deleteFileResponse
    }

    override suspend fun uploadData(user: User, fileName: String, data: ByteArray): Resource<Unit> {
        return uploadDataResponse
    }

}
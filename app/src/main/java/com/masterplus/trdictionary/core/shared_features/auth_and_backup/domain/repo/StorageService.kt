package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo

import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.BackupMeta

interface StorageService {

    suspend fun getFiles(user: User): Resource<List<BackupMeta>>

    suspend fun getFileData(user: User, fileName: String): Resource<ByteArray>

    suspend fun deleteFile(user: User, fileName: String): Resource<Unit>

    suspend fun uploadData(user: User, fileName: String, data: ByteArray): Resource<Unit>
}
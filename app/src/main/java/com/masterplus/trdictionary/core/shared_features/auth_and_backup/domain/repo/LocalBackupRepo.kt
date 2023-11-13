package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo

interface LocalBackupRepo {

    suspend fun getJsonData(): String

    suspend fun fromJsonData(data: String, removeAllData: Boolean, addOnLocalData: Boolean)

    suspend fun deleteAllLocalUserData()
}
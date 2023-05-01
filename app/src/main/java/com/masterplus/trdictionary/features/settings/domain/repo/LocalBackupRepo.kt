package com.masterplus.trdictionary.features.settings.domain.repo

interface LocalBackupRepo {

    suspend fun getJsonData(): String

    suspend fun fromJsonData(data: String, removeAllData: Boolean, addOnLocalData: Boolean)

    suspend fun deleteAllLocalUserData()
}
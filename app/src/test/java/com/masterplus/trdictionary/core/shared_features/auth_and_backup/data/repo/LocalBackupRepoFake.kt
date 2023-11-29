package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo

import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.LocalBackupRepo
import org.junit.jupiter.api.Assertions.*

class LocalBackupRepoFake: LocalBackupRepo{

    var returnedJsonData: String? = "data"

    override suspend fun getJsonData(): String? {
        return returnedJsonData
    }

    override suspend fun fromJsonData(
        data: String,
        removeAllData: Boolean,
        addOnLocalData: Boolean
    ) {

    }

    override suspend fun deleteAllLocalUserData() {

    }

}
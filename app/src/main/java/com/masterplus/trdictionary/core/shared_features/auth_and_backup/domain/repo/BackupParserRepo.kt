package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo

import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.AllBackupData

interface BackupParserRepo {


    fun toJson(backupData: AllBackupData): String?

    fun fromJson(data: String): AllBackupData?

}
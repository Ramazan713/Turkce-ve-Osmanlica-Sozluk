package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo

import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.dtos.AllBackupData

interface BackupParserRepo {


    fun toJson(backupData: AllBackupData): String?

    fun fromJson(data: String): AllBackupData?

}
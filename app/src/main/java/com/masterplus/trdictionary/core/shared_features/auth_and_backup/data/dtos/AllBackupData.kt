package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AllBackupData(
    val histories:  List<HistoryBackup>,
    val lists: List<ListBackup>,
    val listWords: List<ListWordsBackup>,
    val savePoints: List<SavePointBackup>,
    val settingsPreferences: SettingsDataBackup?
)

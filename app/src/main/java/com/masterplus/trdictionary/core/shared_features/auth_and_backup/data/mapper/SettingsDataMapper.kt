package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper

import com.masterplus.trdictionary.core.domain.preferences.model.SettingsData
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.dtos.SettingsDataBackup


fun SettingsData.toBackupData(): SettingsDataBackup {
    return SettingsDataBackup(
        themeEnum = themeEnum,
        useThemeDynamic = useThemeDynamic,
        useArchiveLikeList = useArchiveLikeList,
        showBackupSectionForLogin = showBackupSectionForLogin,
        searchResultCount = searchResultCount,
    )
}


fun SettingsDataBackup.toData(): SettingsData{
    return SettingsData(
        themeEnum = themeEnum,
        useThemeDynamic = useThemeDynamic,
        useArchiveLikeList = useArchiveLikeList,
        showBackupSectionForLogin = showBackupSectionForLogin,
        searchResultCount = searchResultCount,
    )
}
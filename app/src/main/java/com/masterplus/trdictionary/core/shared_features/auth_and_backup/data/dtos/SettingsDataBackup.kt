package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.dtos

import com.masterplus.trdictionary.core.domain.enums.ThemeEnum
import kotlinx.serialization.Serializable

@Serializable
data class SettingsDataBackup(
    val themeEnum: ThemeEnum,
    val useThemeDynamic: Boolean,
    val useArchiveLikeList: Boolean,
    val showBackupSectionForLogin: Boolean,
    val searchResultCount: Int
)

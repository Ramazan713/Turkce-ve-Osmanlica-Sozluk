package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.cloud_backup_settings

import com.masterplus.trdictionary.core.util.UiText

data class CloudBackupState(
    val isLoading: Boolean = false,
    val message: UiText? = null
)

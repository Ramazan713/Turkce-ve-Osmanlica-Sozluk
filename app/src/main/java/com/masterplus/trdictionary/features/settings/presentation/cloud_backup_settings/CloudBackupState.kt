package com.masterplus.trdictionary.features.settings.presentation.cloud_backup_settings

import com.masterplus.trdictionary.core.util.UiText

data class CloudBackupState(
    val isLoading: Boolean = false,
    val message: UiText? = null
)

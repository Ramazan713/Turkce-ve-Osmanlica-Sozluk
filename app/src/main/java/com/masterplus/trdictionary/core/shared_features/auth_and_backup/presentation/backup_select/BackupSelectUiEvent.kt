package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.backup_select

sealed class BackupSelectUiEvent{
    data object RestartApp: BackupSelectUiEvent()
}

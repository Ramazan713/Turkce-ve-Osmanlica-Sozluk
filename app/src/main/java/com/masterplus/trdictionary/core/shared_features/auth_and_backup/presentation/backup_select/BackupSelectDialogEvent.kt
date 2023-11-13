package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.backup_select

sealed class BackupSelectDialogEvent{

    data object AskOverrideBackup: BackupSelectDialogEvent()

    data object AskAddOnBackup: BackupSelectDialogEvent()
}

package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.backup_select

import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.BackupMeta

sealed class SelectBackupEvent{
    data object Refresh: SelectBackupEvent()

    data class SelectItem(val backupMeta: BackupMeta): SelectBackupEvent()

    data object OverrideBackup: SelectBackupEvent()

    data object AddTopOfBackup: SelectBackupEvent()

    data class ShowDialog(val showDialog: Boolean, val dialogEvent: BackupSelectDialogEvent? = null ): SelectBackupEvent()

    data object ClearUiEvent: SelectBackupEvent()

    data object ClearMessage: SelectBackupEvent()
}

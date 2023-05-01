package com.masterplus.trdictionary.features.settings.presentation.backup_select

import com.masterplus.trdictionary.core.domain.util.UiText
import com.masterplus.trdictionary.features.settings.domain.model.BackupMeta

data class SelectBackupState(
    val items: List<BackupMeta> = emptyList(),
    val selectedItem: BackupMeta? = null,
    val isRefreshEnabled: Boolean = true,
    val refreshSeconds: Int = 0,
    val isLoading: Boolean = false,
    val showDialog: Boolean = false,
    val dialogEvent: BackupSelectDialogEvent? = null,
    val uiEvent: BackupSelectUiEvent? = null,
    val message: UiText? = null
)

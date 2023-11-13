package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth

sealed interface AuthUiAction {

    data object ShowBackupSectionForLogin: AuthUiAction

    data object RefreshApp: AuthUiAction
}
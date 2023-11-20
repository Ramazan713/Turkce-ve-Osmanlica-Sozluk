package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth

import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.core.domain.utils.UiText

data class AuthState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val message: UiText? = null,
    val dialogEvent: AuthDialogEvent? = null,
    val uiAction: AuthUiAction? = null
)

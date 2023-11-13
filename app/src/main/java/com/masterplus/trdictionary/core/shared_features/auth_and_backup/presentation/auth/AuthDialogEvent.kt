package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth

sealed interface AuthDialogEvent {
    data class EnterEmailForResetPassword(
        val email: String,
        val onResult: (String) -> Unit
    ): AuthDialogEvent
}
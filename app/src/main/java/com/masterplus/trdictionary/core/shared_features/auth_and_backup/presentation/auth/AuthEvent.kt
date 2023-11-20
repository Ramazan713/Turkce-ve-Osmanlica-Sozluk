package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth

import com.google.firebase.auth.AuthCredential
import com.masterplus.trdictionary.core.domain.utils.UiText

sealed interface AuthEvent {

    data class SignInWithEmail(val email: String, val password: String): AuthEvent

    data class SignUpWithEmail(val email: String, val password: String): AuthEvent

    data class ResetPassword(val email: String): AuthEvent

    data class SignInWithCredential(val credential: AuthCredential): AuthEvent

    data class ShowMessage(val message: UiText?): AuthEvent

    data object ClearMessage: AuthEvent

    data object ClearUiAction: AuthEvent

    data class ShowDialog(val dialogEvent: AuthDialogEvent?): AuthEvent

    data class SignOut(val backupBeforeSignOut: Boolean): AuthEvent

    data object LoadLastBackup: AuthEvent

    data object DeleteAllUserData: AuthEvent

}
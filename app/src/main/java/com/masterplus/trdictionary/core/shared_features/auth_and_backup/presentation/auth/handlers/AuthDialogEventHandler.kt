package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.handlers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowGetTextDialog
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.AuthDialogEvent
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.AuthEvent

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AuthDialogEventHandler(
    dialogEvent: AuthDialogEvent,
    onEvent: (AuthEvent) -> Unit
) {

    fun close(){
        onEvent(AuthEvent.ShowDialog(null))
    }

    when(dialogEvent){
        is AuthDialogEvent.EnterEmailForResetPassword -> {
            ShowGetTextDialog(
                title = stringResource(id = R.string.enter_email_for_reset_password),
                onApproved = dialogEvent.onResult,
                onClosed = ::close,
                content = dialogEvent.email,
            )
        }
    }

}
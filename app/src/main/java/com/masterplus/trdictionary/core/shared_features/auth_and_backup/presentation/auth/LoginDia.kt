package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.DialogHeader
import com.masterplus.trdictionary.core.presentation.dialog_body.CustomDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.LoadingDialog
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components.AuthProvidersComponent
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components.EmailAuthProviderComponent
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components.OrDivider
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.handlers.AuthDialogEventHandler

@Composable
fun LoginDia(
    onEvent: (AuthEvent) -> Unit,
    state: AuthState,
    windowWidthSizeClass: WindowWidthSizeClass,
    isDarkMode: Boolean,
    onClose: () -> Unit,
){
    val context = LocalContext.current
    val firebaseAuth = remember {
        FirebaseAuth.getInstance()
    }
    LaunchedEffect(state.user){
        if(state.user != null){
            onClose()
        }
    }
    LoginDiaUI(
        onEvent = onEvent,
        state = state,
        windowWidthSizeClass = windowWidthSizeClass,
        isDarkMode = isDarkMode,
        onClose = onClose,
        onSignInWithOAuthProvider = { oAuthProvider ->
            firebaseAuth.startActivityForSignInWithProvider(context as Activity,oAuthProvider).addOnCompleteListener {
                it.result?.credential?.let { credential ->
                    onEvent(AuthEvent.SignInWithCredential(credential))
                }
            }
        }
    )
}


@Composable
private fun LoginDiaUI(
    onEvent: (AuthEvent) -> Unit,
    state: AuthState,
    windowWidthSizeClass: WindowWidthSizeClass,
    isDarkMode: Boolean,
    onClose: () -> Unit,
    onSignInWithOAuthProvider: (OAuthProvider) -> Unit,
) {
    val currentOnEvent by rememberUpdatedState(newValue = onEvent)


    CustomDialog(
        onClosed = onClose,
        adaptiveWidthSizeClass = windowWidthSizeClass
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .padding(bottom = 16.dp)
        ) {
            DialogHeader(
                title = stringResource(id = R.string.sign_in_c),
                onIconClick = onClose
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 4.dp)
            ) {

                item {
                    EmailAuthProviderComponent(
                        onForgetPassword = { email ->
                            onEvent(AuthEvent.ShowDialog(
                                AuthDialogEvent.EnterEmailForResetPassword(
                                    email = email,
                                    onResult = {newEmail->
                                        onEvent(AuthEvent.ResetPassword(newEmail))
                                    }
                                )
                            ))
                        },
                        onSignIn = { email, password ->
                            onEvent(AuthEvent.SignInWithEmail(email, password))
                        },
                        onSignUp = { email, password ->
                            onEvent(AuthEvent.SignUpWithEmail(email, password))
                        },
                    )
                }

                item {
                    OrDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp)
                    )
                }

                item {
                    AuthProvidersComponent(
                        isDarkMode = isDarkMode,
                        onSignInWithCredential = { credential->
                            currentOnEvent(AuthEvent.SignInWithCredential(credential))
                        },
                        onSignInWithOAuthProvider = onSignInWithOAuthProvider,
                        onError = {
                            currentOnEvent(AuthEvent.ShowMessage(it))
                        }
                    )
                }
            }
        }
    }

    state.dialogEvent?.let { dialogEvent->
        AuthDialogEventHandler(dialogEvent = dialogEvent, onEvent = onEvent)
    }

    if(state.isLoading){
        LoadingDialog()
    }

}



@Preview
@Composable
private fun LoginPreview() {
    LoginDiaUI(
        onEvent = {},
        state = AuthState(),
        windowWidthSizeClass = WindowWidthSizeClass.Compact,
        isDarkMode = false,
        onClose = {},
        onSignInWithOAuthProvider = {}
    )
}

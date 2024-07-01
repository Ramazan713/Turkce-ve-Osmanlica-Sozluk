package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.presentation.components.DialogHeader
import com.masterplus.trdictionary.core.presentation.dialog_body.CustomDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.LoadingDialog
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components.AuthProvidersComponent
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components.EmailAuthProviderComponent
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components.EmailAuthProviderStyles
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components.EmailString
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components.OrDivider
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components.PasswordString


@Composable
fun ShowDeleteAccountDia(
    onEvent: (AuthEvent) -> Unit,
    state: AuthState,
    windowWidthSizeClass: WindowWidthSizeClass,
    isDarkMode: Boolean,
    onClose: () -> Unit,
) {

    LaunchedEffect(state.user) {
        if(state.user == null){
            onClose()
        }
    }

    ReAuthenticateUserDia(
        onEvent = onEvent,
        state = state,
        windowWidthSizeClass = windowWidthSizeClass,
        isDarkMode = isDarkMode,
        onClose = onClose,
        onReAuthenticateWithCredential = {
            onEvent(AuthEvent.DeleteUserWithCredentials(it))
        },
        onReAuthenticateWithEmail = { email, password ->
            onEvent(AuthEvent.DeleteUserWithEmail(email, password))
        }
    )
}


@Composable
fun ReAuthenticateUserDia(
    onReAuthenticateWithCredential: (AuthCredential) -> Unit,
    onReAuthenticateWithEmail: (EmailString, PasswordString) -> Unit,
    onEvent: (AuthEvent) -> Unit,
    state: AuthState,
    windowWidthSizeClass: WindowWidthSizeClass,
    isDarkMode: Boolean,
    onClose: () -> Unit,
) {
    val context = LocalContext.current
    val firebaseAuth = remember {
        FirebaseAuth.getInstance()
    }

    if(state.isLoading){
        LoadingDialog()
    }

    ReAuthenticateUserDiaUI(
        windowWidthSizeClass = windowWidthSizeClass,
        isDarkMode = isDarkMode,
        onClose = onClose,
        onSignInWithOAuthProvider = { oAuthProvider ->
            firebaseAuth.currentUser?.startActivityForReauthenticateWithProvider(context as Activity,oAuthProvider)?.addOnCompleteListener {
                it.result?.credential?.let { credential ->
                    onReAuthenticateWithCredential(credential)
                }
            }
        },
        onError = {
            onEvent(AuthEvent.ShowMessage(it))
        },
        onSignInWithCredential = onReAuthenticateWithCredential,
        onReAuthenticateWithEmail = onReAuthenticateWithEmail
    )
}



@Composable
private fun ReAuthenticateUserDiaUI(
    windowWidthSizeClass: WindowWidthSizeClass,
    isDarkMode: Boolean,
    onClose: () -> Unit,
    onSignInWithOAuthProvider: (OAuthProvider) -> Unit,
    onSignInWithCredential: (AuthCredential) -> Unit,
    onReAuthenticateWithEmail: (EmailString, PasswordString) -> Unit,
    onError: (UiText) -> Unit,
) {

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
                        onSignIn = onReAuthenticateWithEmail,
                        providerStyles = EmailAuthProviderStyles(
                            showSignUp = false,
                            showForgetPassword = false
                        )
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
                        onSignInWithCredential = onSignInWithCredential,
                        onSignInWithOAuthProvider = onSignInWithOAuthProvider,
                        onError = onError
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun ReAuthenticateUserDiaUIPreview() {
    ReAuthenticateUserDiaUI(
        windowWidthSizeClass = WindowWidthSizeClass.Compact,
        onClose = {},
        isDarkMode = false,
        onSignInWithOAuthProvider = {},
        onSignInWithCredential = {},
        onError = {},
        onReAuthenticateWithEmail = {x,y->}
    )
}
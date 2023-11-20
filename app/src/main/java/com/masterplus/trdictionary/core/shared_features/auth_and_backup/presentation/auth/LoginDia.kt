package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.masterplus.trdictionary.BuildConfig
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.DefaultIconToggleButton
import com.masterplus.trdictionary.core.presentation.components.DialogHeader
import com.masterplus.trdictionary.core.presentation.dialog_body.CustomDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.LoadingDialog
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components.AuthButton
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components.AuthProviderRow
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.handlers.AuthDialogEventHandler
import com.masterplus.trdictionary.core.domain.utils.UiText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun LoginDia(
    onEvent: (AuthEvent) -> Unit,
    state: AuthState,
    windowWidthSizeClass: WindowWidthSizeClass,
    isDarkMode: Boolean,
    onClose: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentOnEvent by rememberUpdatedState(newValue = onEvent)

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    val githubProvider = remember {
        OAuthProvider.newBuilder("github.com")
    }

    val twitterProvider = remember {
        OAuthProvider.newBuilder("twitter.com")
    }

    val oneTapClient = remember {
        Identity.getSignInClient(context)
    }

    val firebaseAuth = remember {
        FirebaseAuth.getInstance()
    }

    var launchAuthProvider by remember {
        mutableStateOf<OAuthProvider?>(null)
    }

    LaunchedEffect(state.user){
        if(state.user != null){
            onClose()
        }
    }


    val oneTapGoogleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {activityResult->
            try {
                val signInCredential = oneTapClient.getSignInCredentialFromIntent(activityResult.data)
                val idToken = signInCredential.googleIdToken
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                currentOnEvent(AuthEvent.SignInWithCredential(credential))
            }catch (e: Exception){
                currentOnEvent(AuthEvent.ShowMessage(UiText.Text(e.localizedMessage?: return@rememberLauncherForActivityResult)))
            }
        }
    )

    LaunchedEffect(launchAuthProvider,lifecycleOwner.lifecycle){
        snapshotFlow { launchAuthProvider }
            .filterNotNull()
            .distinctUntilChanged()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { provider->
                firebaseAuth.startActivityForSignInWithProvider(context as Activity,provider).addOnCompleteListener {
                    if(it.isSuccessful){
                        it.result.credential?.let { credential->
                            currentOnEvent(AuthEvent.SignInWithCredential(credential))
                        }
                    }
                    launchAuthProvider = null
                }
            }
    }

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
                    EmailField(
                        modifier = Modifier.fillMaxWidth(),
                        email = email,
                        onEmailChange = { email = it }
                    )
                }

                item {
                    PasswordField(
                        modifier = Modifier.fillMaxWidth(),
                        password = password,
                        onPasswordChange = { password = it }
                    )
                }

                item {
                    ForgetPassword(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onEvent(AuthEvent.ShowDialog(
                                AuthDialogEvent.EnterEmailForResetPassword(
                                    email = email,
                                    onResult = {newEmail->
                                        onEvent(AuthEvent.ResetPassword(newEmail))
                                    }
                                )
                            ))
                        }
                    )
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        AuthButton(
                            title = stringResource(id = R.string.sign_up_c),
                            modifier = Modifier.weight(1f),
                            onClick = { onEvent(AuthEvent.SignUpWithEmail(email, password)) }
                        )
                        AuthButton(
                            title = stringResource(id = R.string.sign_in_c),
                            modifier = Modifier.weight(1f),
                            onClick = { onEvent(AuthEvent.SignInWithEmail(email, password)) }
                        )
                    }

                    Spacer(modifier = Modifier.height(64.dp))

                }

                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AuthProviderRow(
                            resId = R.drawable.google_icon,
                            title = stringResource(id = R.string.login_with_n_c,"Google"),
                            isDarkMode = isDarkMode,
                            onClick = {
                                val signInRequest = BeginSignInRequest.builder()
                                    .setGoogleIdTokenRequestOptions(
                                        BeginSignInRequest.GoogleIdTokenRequestOptions
                                            .builder()
                                            .setServerClientId(BuildConfig.AUTH_CLIENT_ID)
                                            .setSupported(true)
                                            .setFilterByAuthorizedAccounts(false)
                                            .build()
                                    )
                                    .setAutoSelectEnabled(true)
                                    .build()

                                oneTapClient.beginSignIn(signInRequest).addOnSuccessListener {
                                    oneTapGoogleLauncher.launch(IntentSenderRequest.Builder(it.pendingIntent).build())
                                }

                            }
                        )

                        AuthProviderRow(
                            resId = R.drawable.x_icon,
                            darkResId = R.drawable.x_icon_dark,
                            title = stringResource(id = R.string.login_with_n_c,"X"),
                            isDarkMode = isDarkMode,
                            onClick = {
                                launchAuthProvider = twitterProvider.build()
                            }
                        )

                        AuthProviderRow(
                            resId = R.drawable.github_icon,
                            darkResId = R.drawable.github_icon_dark,
                            isDarkMode = isDarkMode,
                            title = stringResource(id = R.string.login_with_n_c,"Github"),
                            onClick = {
                                launchAuthProvider = githubProvider.build()
                            }
                        )
                    }
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

@Composable
private fun ForgetPassword(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier
    ) {
        TextButton(
            onClick = onClick
        ) {
            Text(
                text = stringResource(id = R.string.forget_password),
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}

@Composable
private fun PasswordField(
    modifier: Modifier = Modifier,
    password: String,
    onPasswordChange: (String) -> Unit,
) {

    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = modifier,
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(text = stringResource(id = R.string.password)) },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Password, contentDescription = stringResource(id = R.string.password))
        },
        visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            DefaultIconToggleButton(
                value = showPassword,
                onValueChange = { showPassword = it },
                imageVector = Icons.Default.VisibilityOff,
                selectedImageVector = Icons.Default.Visibility,
            )
        }
    )
}

@Composable
private fun EmailField(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = email,
        onValueChange = onEmailChange,
        label = { Text(text = stringResource(id = R.string.email)) },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Email, contentDescription = stringResource(id = R.string.email))
        }
    )
}




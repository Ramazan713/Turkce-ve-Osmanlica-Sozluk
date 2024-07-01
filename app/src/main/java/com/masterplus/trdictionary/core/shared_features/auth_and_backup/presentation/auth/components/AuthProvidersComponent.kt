package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.masterplus.trdictionary.BuildConfig
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.presentation.utils.EventHandler

@Composable
fun AuthProvidersComponent(
    isDarkMode: Boolean,
    onSignInWithCredential: (AuthCredential) -> Unit,
    onSignInWithOAuthProvider: (OAuthProvider) -> Unit,
    onError: (UiText) -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(12.dp)
) {
    val context = LocalContext.current

    var launchAuthProvider by remember {
        mutableStateOf<OAuthProvider?>(null)
    }

    val githubProvider = remember {
        OAuthProvider.newBuilder("github.com").build()
    }

    val twitterProvider = remember {
        OAuthProvider.newBuilder("twitter.com").build()
    }

    val oneTapClient = remember {
        Identity.getSignInClient(context)
    }

    val oneTapGoogleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {activityResult->
            try {
                val signInCredential = oneTapClient.getSignInCredentialFromIntent(activityResult.data)
                val idToken = signInCredential.googleIdToken
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                onSignInWithCredential(credential)
            }catch (e: Exception){
                onError(UiText.Text(e.localizedMessage?: return@rememberLauncherForActivityResult))
            }
        }
    )

    EventHandler(event = launchAuthProvider) { authProvider ->
        onSignInWithOAuthProvider(authProvider)
        launchAuthProvider = null
    }


    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement
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
                launchAuthProvider = twitterProvider
            }
        )

        AuthProviderRow(
            resId = R.drawable.github_icon,
            darkResId = R.drawable.github_icon_dark,
            isDarkMode = isDarkMode,
            title = stringResource(id = R.string.login_with_n_c,"Github"),
            onClick = {
                launchAuthProvider = githubProvider
            }
        )
    }
}
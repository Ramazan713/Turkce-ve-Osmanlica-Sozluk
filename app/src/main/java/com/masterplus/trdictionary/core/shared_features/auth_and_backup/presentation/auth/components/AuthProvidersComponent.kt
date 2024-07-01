package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.masterplus.trdictionary.BuildConfig
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.presentation.utils.EventHandler
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()

    var launchAuthProvider by remember {
        mutableStateOf<OAuthProvider?>(null)
    }

    val githubProvider = remember {
        OAuthProvider.newBuilder("github.com")
    }

    val twitterProvider = remember {
        OAuthProvider.newBuilder("twitter.com")
    }

    val credentialManager = remember {
        CredentialManager.create(context)
    }

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
                scope.launch {
                    val result = handleGoogleSignIn(
                        credentialManager = credentialManager,
                        context = context
                    )
                    result.getSuccessData?.let(onSignInWithCredential)
                    result.getError?.let(onError)
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

private suspend fun handleGoogleSignIn(
    credentialManager: CredentialManager,
    context: Context
): Resource<AuthCredential>{
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.AUTH_CLIENT_ID)
        .setAutoSelectEnabled(false)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()
    try{
        val result = credentialManager.getCredential(
            request = request,
            context = context,
        )
        when(val credential = result.credential){
            is CustomCredential -> {
                try {
                    if(credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                        return Resource.Success(authCredential)
                    }
                }catch (e: GoogleIdTokenParsingException){
                    return Resource.Error(UiText.Text(e.localizedMessage ?: "Error"))
                }
            }
        }

    }catch (e: GetCredentialException){
        return Resource.Error(UiText.Text(e.localizedMessage ?: "Error"))
    }
    return Resource.Error(UiText.Resource(R.string.something_went_wrong))
}

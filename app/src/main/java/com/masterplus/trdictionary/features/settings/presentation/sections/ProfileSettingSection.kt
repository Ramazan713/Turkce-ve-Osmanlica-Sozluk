package com.masterplus.trdictionary.features.settings.presentation.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.masterplus.trdictionary.core.presentation.components.buttons.PrimaryButton
import com.masterplus.trdictionary.features.settings.presentation.SettingEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingState
import com.masterplus.trdictionary.features.settings.presentation.components.UnRegisteredProfileIcon
import com.masterplus.trdictionary.R

@Composable
fun ProfileSettingSection(
    state: SettingState,
    onEvent: (SettingEvent)->Unit,
){
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(vertical = 13.dp)
    ) {

        state.user?.let { user->
            user.photoUri?.let { uri->
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(uri.toString())
                        .diskCacheKey(state.user.uid)
                        .transformations(
                            CircleCropTransformation()
                        )
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.size(156.dp),
                )

                user.name?.let { name->
                    Text(
                        name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
                user.email?.let { email->
                    Text(
                        email,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }

            }?: kotlin.run {
                UnRegisteredProfileIcon()
            }
        }?: kotlin.run {
            UnRegisteredProfileIcon()

            PrimaryButton(
                title = stringResource(R.string.sign_in),
                onClick = {
                    onEvent(SettingEvent.SignInLaunch)
                },
            )
        }

    }
}
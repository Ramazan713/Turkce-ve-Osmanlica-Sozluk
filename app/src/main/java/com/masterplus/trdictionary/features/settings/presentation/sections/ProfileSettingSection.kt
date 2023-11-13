package com.masterplus.trdictionary.features.settings.presentation.sections

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.masterplus.trdictionary.features.settings.presentation.SettingEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingState
import com.masterplus.trdictionary.features.settings.presentation.components.UnRegisteredProfileIcon
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.features.settings.presentation.SettingDialogEvent

@Composable
fun ProfileSettingSection(
    user: User?,
    onEvent: (SettingEvent)->Unit,
){
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 13.dp)
    ) {

        if(user != null){
            val photoUri = user.photoUri

            if(photoUri != null){
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(photoUri.toString())
                        .diskCacheKey(user.uid)
                        .transformations(
                            CircleCropTransformation()
                        )
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.size(156.dp),
                )
            }else{
                UnRegisteredProfileIcon()
            }

            user.name?.let { name->
                if(name.isNotBlank()){
                    Text(
                        name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }
            user.email?.let { email->
                if(email.isNotBlank()){
                    Text(
                        email,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }
        }else{
            UnRegisteredProfileIcon()

            Button(onClick = { onEvent(SettingEvent.ShowDialog(SettingDialogEvent.ShowAuthDia)) }) {
                Text(text = stringResource(R.string.sign_in))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSettingSectionPreview() {
    ProfileSettingSection(
        user = null,
        onEvent = {}
    )
}
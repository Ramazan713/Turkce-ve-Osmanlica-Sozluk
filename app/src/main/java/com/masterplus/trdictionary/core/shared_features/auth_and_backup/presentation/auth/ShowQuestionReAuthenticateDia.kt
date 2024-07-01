package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R


@Composable
fun ShowQuestionReAuthenticateDia(
    onApprove: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(
                text = stringResource(id = R.string.login_again_to_continue),
            )
        },
        confirmButton = {
            Button(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                onClick = {
                    onCancel()
                    onApprove()
                }
            ) {
                Text(text = stringResource(R.string.sign_in_c))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel,
                modifier = Modifier
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}
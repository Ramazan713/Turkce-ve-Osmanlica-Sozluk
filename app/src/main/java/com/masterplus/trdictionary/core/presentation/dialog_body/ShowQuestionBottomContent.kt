package com.masterplus.trdictionary.core.presentation.dialog_body

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.masterplus.trdictionary.R

@Composable
fun ShowQuestionDialog(
    title: String? = null,
    content: String? = null,
    onApproved: () -> Unit,
    allowDismiss: Boolean = true,
    onClosed: () -> Unit,
    negativeTitle: String = stringResource(R.string.cancel),
    positiveTitle: String = stringResource(R.string.approve),
    iconVector: ImageVector? = Icons.Default.Warning
){
    
    AlertDialog(
        properties = DialogProperties(
            dismissOnClickOutside = allowDismiss,
            dismissOnBackPress = allowDismiss
        ),
        onDismissRequest = onClosed,
        title = {
            Text(
                text = title ?: return@AlertDialog,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text =  { Text(text = content ?: return@AlertDialog )},
        dismissButton = {
            TextButton(
                onClick = { onClosed() },
            ){
                Text(negativeTitle)
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    onClosed()
                    onApproved()
                },
            ){
                Text(positiveTitle)
            }
        },
        icon = {
            Icon(imageVector = iconVector ?: return@AlertDialog, contentDescription = null)
        }
    )
}
package com.masterplus.trdictionary.core.presentation.dialog_body

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CustomDialog(
    onClosed: () -> Unit,
    modifier: Modifier = Modifier,
    allowDismiss: Boolean = true,
    usePlatformDefaultWidth: Boolean = true,
    adaptiveWidthSizeClass: WindowWidthSizeClass? = null,
    content: @Composable () -> Unit,
){

    val platformDefaultWidth = if(adaptiveWidthSizeClass == null) usePlatformDefaultWidth
        else adaptiveWidthSizeClass != WindowWidthSizeClass.Compact

    Dialog(
        onDismissRequest = onClosed,
        properties = DialogProperties(
            usePlatformDefaultWidth = platformDefaultWidth,
            dismissOnBackPress = allowDismiss,
            dismissOnClickOutside = allowDismiss
        )
    ){
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium),
            color = MaterialTheme.colorScheme.surfaceVariant
        ){
            content()
        }
    }
}
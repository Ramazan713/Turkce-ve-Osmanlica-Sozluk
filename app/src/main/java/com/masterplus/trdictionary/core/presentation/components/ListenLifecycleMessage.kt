package com.masterplus.trdictionary.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.core.domain.util.ToastHelper
import com.masterplus.trdictionary.core.domain.util.UiText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun ListenLifecycleMessage(
    message: UiText?,
    onDismiss: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(message,lifecycleOwner){
        snapshotFlow { message }
            .filterNotNull()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest {message->
                ToastHelper.showMessage(message,context)
                onDismiss()
            }
    }
}
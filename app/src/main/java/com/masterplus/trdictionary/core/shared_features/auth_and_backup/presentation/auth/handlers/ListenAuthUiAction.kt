package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.handlers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.core.presentation.utils.EventHandler
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.AuthUiAction
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun ListenAuthUiAction(
    uiAction: AuthUiAction?,
    onClose: (AuthUiAction) -> Unit
) {
    val currentOnClose by rememberUpdatedState(newValue = onClose)
    EventHandler(event = uiAction) {
        currentOnClose(it)
    }
}
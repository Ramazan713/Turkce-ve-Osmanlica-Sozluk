package com.masterplus.trdictionary.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun<T> EventHandler(
    event: T?,
    onEvent: suspend (T) -> Unit,
) {
    val currentOnEvent by rememberUpdatedState(newValue = onEvent)
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(event, lifecycle.lifecycle) {
        snapshotFlow { event }
            .flowWithLifecycle(lifecycle.lifecycle)
            .distinctUntilChanged()
            .filterNotNull()
            .collectLatest {
                currentOnEvent(it)
            }
    }
}
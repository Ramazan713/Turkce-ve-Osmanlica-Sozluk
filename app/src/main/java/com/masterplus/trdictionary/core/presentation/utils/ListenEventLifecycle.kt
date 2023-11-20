package com.masterplus.trdictionary.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun ListenEventLifecycle(
    onCreate: (() -> Unit)? = null,
    onStart: (() -> Unit)? = null,
    onResume: (() -> Unit)? = null,
    onPause: (() -> Unit)? = null,
    onStop: (() -> Unit)? = null,
    onDestroy: (() -> Unit)? = null,
    onAny: (() -> Unit)? = null,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentOnCreate by rememberUpdatedState(newValue = onCreate)
    val currentOnStart by rememberUpdatedState(newValue = onStart)
    val currentOnResume by rememberUpdatedState(newValue = onResume)
    val currentOnPause by rememberUpdatedState(newValue = onPause)
    val currentOnStop by rememberUpdatedState(newValue = onStop)
    val currentOnDestroy by rememberUpdatedState(newValue = onDestroy)
    val currentOnAny by rememberUpdatedState(newValue = onAny)


    DisposableEffect(lifecycleOwner.lifecycle){

        val listener = LifecycleEventObserver { _, event ->
            when(event){
                Lifecycle.Event.ON_CREATE -> {
                    currentOnCreate?.invoke()
                }
                Lifecycle.Event.ON_START -> {
                    currentOnStart?.invoke()
                }
                Lifecycle.Event.ON_RESUME -> {
                    currentOnResume?.invoke()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    currentOnPause?.invoke()
                }
                Lifecycle.Event.ON_STOP -> {
                    currentOnStop?.invoke()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    currentOnDestroy?.invoke()
                }
                Lifecycle.Event.ON_ANY -> {
                    currentOnAny?.invoke()
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(listener)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(listener)
        }
    }
}
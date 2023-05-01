package com.masterplus.trdictionary.features.app.presentation.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.features.app.presentation.in_app.InAppEvent
import com.masterplus.trdictionary.features.app.presentation.in_app.InAppFeaturesState
import com.masterplus.trdictionary.features.app.presentation.in_app.InAppUiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InAppFeaturesControl(
    state: InAppFeaturesState,
    onEvent: (InAppEvent) -> Unit
){
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    state.uiEvent?.let { uiEvent->
        LaunchedEffect(uiEvent,lifecycle){
            snapshotFlow { uiEvent }
                .flowWithLifecycle(lifecycle)
                .collectLatest { event->
                    when(event){
                        is InAppUiEvent.ShowReviewApi -> {
                            event.manager.launchReviewFlow(context as Activity,event.reviewInfo)
                        }
                    }
                    onEvent(InAppEvent.ClearUiEvent)
                }
        }
    }

}
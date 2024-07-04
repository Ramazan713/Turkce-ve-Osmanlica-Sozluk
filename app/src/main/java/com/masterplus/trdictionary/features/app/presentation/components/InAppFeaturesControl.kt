package com.masterplus.trdictionary.features.app.presentation.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.masterplus.trdictionary.core.presentation.utils.EventHandler
import com.masterplus.trdictionary.features.app.presentation.in_app.InAppEvent
import com.masterplus.trdictionary.features.app.presentation.in_app.InAppFeaturesState
import com.masterplus.trdictionary.features.app.presentation.in_app.InAppUiEvent

@Composable
fun InAppFeaturesControl(
    state: InAppFeaturesState,
    onEvent: (InAppEvent) -> Unit
){
    val context = LocalContext.current

    EventHandler(event = state.uiEvent) { event->
        when(event){
            is InAppUiEvent.ShowReviewApi -> {
                event.manager.launchReviewFlow(context as Activity,event.reviewInfo)
            }
        }
        onEvent(InAppEvent.ClearUiEvent)
    }
}
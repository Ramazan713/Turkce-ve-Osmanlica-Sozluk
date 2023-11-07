package com.masterplus.trdictionary.core.shared_features.share.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.extensions.copyClipboardText
import com.masterplus.trdictionary.core.extensions.share
import com.masterplus.trdictionary.core.extensions.shareText
import com.masterplus.trdictionary.core.shared_features.share.domain.use_cases.ShareWordUseCases
import com.masterplus.trdictionary.core.util.ToastHelper
import com.masterplus.trdictionary.core.util.UiText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun ShareWordEventHandler(
    event: ShareWordUseCases.ShareWordResult?,
    onClearEvent: () -> Unit
) {

    val currentOnClearEvent by rememberUpdatedState(newValue = onClearEvent)

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(event,lifecycleOwner.lifecycle){
        snapshotFlow { event }
            .filterNotNull()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { event->
                currentOnClearEvent()
                when(event){
                    is ShareWordUseCases.ShareWordResult.CopyWord -> {
                        event.wordText.copyClipboardText(clipboardManager)
                        ToastHelper.showMessage(UiText.Resource(R.string.copied),context)
                    }
                    is ShareWordUseCases.ShareWordResult.ShareWord -> {
                        event.wordText.shareText(context)
                    }
                    is ShareWordUseCases.ShareWordResult.ShareWordWithMeanings -> {
                        event.wordMeanings?.share(context)
                    }
                }
            }
    }

}
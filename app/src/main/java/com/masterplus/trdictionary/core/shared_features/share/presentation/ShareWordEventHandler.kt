package com.masterplus.trdictionary.core.shared_features.share.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.extensions.copyClipboardText
import com.masterplus.trdictionary.core.extensions.share
import com.masterplus.trdictionary.core.extensions.shareText
import com.masterplus.trdictionary.core.presentation.utils.EventHandler
import com.masterplus.trdictionary.core.presentation.utils.ToastHelper
import com.masterplus.trdictionary.core.shared_features.share.domain.use_cases.ShareWordUseCases

@Composable
fun ShareWordEventHandler(
    event: ShareWordUseCases.ShareWordResult?,
    onClearEvent: () -> Unit
) {
    val currentOnClearEvent by rememberUpdatedState(newValue = onClearEvent)
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    EventHandler(event = event) { collectedEvent->
        currentOnClearEvent()
        when(collectedEvent){
            is ShareWordUseCases.ShareWordResult.CopyWord -> {
                collectedEvent.wordText.copyClipboardText(clipboardManager)
                ToastHelper.showMessage(UiText.Resource(R.string.copied),context)
            }
            is ShareWordUseCases.ShareWordResult.ShareWord -> {
                collectedEvent.wordText.shareText(context)
            }
            is ShareWordUseCases.ShareWordResult.ShareWordWithMeanings -> {
                collectedEvent.wordMeanings?.share(context)
            }
        }
    }
}
package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.core.extensions.share
import com.masterplus.trdictionary.core.extensions.shareText
import com.masterplus.trdictionary.features.word_detail.domain.use_case.share.ShareWordUseCases
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WordListSharedUiEventManage(
    sharedState: WordsDetailSharedState,
    onSharedEvent: (WordsDetailSharedEvent)->Unit
){

    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    sharedState.uiEvent?.let { uiEvent->
        LaunchedEffect(uiEvent,lifecycle){
            snapshotFlow { uiEvent }
                .flowWithLifecycle(lifecycle)
                .collectLatest { event->
                    when (event) {
                        is WordsDetailSharedUiEvent.ShareWord -> {
                            when (val shareResult = event.shareResult) {
                                is ShareWordUseCases.ShareWordResult.ShareLink -> {
                                    shareResult.link.shareText(context)
                                }
                                is ShareWordUseCases.ShareWordResult.ShareWord -> {
                                    shareResult.wordMeanings?.wordDetail?.word?.shareText(context)
                                }
                                is ShareWordUseCases.ShareWordResult.ShareWordWithMeanings -> {
                                    shareResult.wordMeanings?.share(context)
                                }
                            }
                        }
                    }
                    onSharedEvent(WordsDetailSharedEvent.ClearUiEvent)
                }
        }
    }

}
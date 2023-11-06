package com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.core.extensions.share
import com.masterplus.trdictionary.core.extensions.shareText
import com.masterplus.trdictionary.core.presentation.features.share.domain.use_cases.ShareWordUseCases
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WordListSharedUiEventManage(
    sharedState: WordListSharedState,
    onSharedEvent: (WordListSharedEvent)->Unit,
){
    val context = LocalContext.current
    val lifeCycle = LocalLifecycleOwner.current.lifecycle

    sharedState.uiEvent?.let { uiEvent->
        LaunchedEffect(uiEvent,lifeCycle){
            snapshotFlow { uiEvent }
                .flowWithLifecycle(lifeCycle)
                .collectLatest { event->
                    when (event) {
                        is WordListSharedUiEvent.ShareWord -> {
//                            when(val shareResult = event.shareResult){
//                                is ShareWordUseCases.ShareWordResult.ShareLink -> {
//                                    shareResult.link.shareText(context)
//                                }
//                                is ShareWordUseCases.ShareWordResult.ShareWord -> {
//                                    shareResult.wordMeanings?.wordDetail?.word?.shareText(context)
//                                }
//                                is ShareWordUseCases.ShareWordResult.ShareWordWithMeanings -> {
//                                    shareResult.wordMeanings?.share(context)
//                                }
//                            }
                        }
                    }
                    onSharedEvent(WordListSharedEvent.ClearUiEvent)
                }
        }
    }

}
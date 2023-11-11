package com.masterplus.trdictionary.features.word_detail.single_word_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.navigation.CustomTopAppBar
import com.masterplus.trdictionary.core.shared_features.share.presentation.ShareWordEventHandler
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailState
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.contents.WordsDetailAdaptiveContent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.handlers.WordsDetailModalEventsHandler
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.handlers.WordsDetailSheetEventsHandler
import com.masterplus.trdictionary.core.util.PreviewDesktop
import com.masterplus.trdictionary.core.util.SampleDatas
import com.masterplus.trdictionary.core.util.ShowLifecycleToastMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleWordDetailPage(
    onNavigateBack: ()->Unit,
    onRelatedWordClicked: (Int)->Unit,
    state: WordsListDetailState,
    onEvent: (WordsListDetailEvent) -> Unit,
    wordWithSimilar: WordWithSimilar?,
    windowWidthSizeClass: WindowWidthSizeClass
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    ShareWordEventHandler(
        event = state.shareResultEvent,
        onClearEvent = { onEvent(WordsListDetailEvent.ClearShareResult) }
    )

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = { onEvent(WordsListDetailEvent.ClearMessage) }
    )

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = wordWithSimilar?.wordDetail?.word ?: "",
                onNavigateBack = onNavigateBack,
                scrollBehavior = scrollBehavior,
            )
        },
    ){padding->
        Box(
            modifier = Modifier
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
        ) {
            if(wordWithSimilar == null){
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = stringResource(id = R.string.no_result),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }else{
                WordsDetailAdaptiveContent(
                    modifier = Modifier.fillMaxSize(),
                    wordWithSimilar = wordWithSimilar,
                    windowWidthSizeClass = windowWidthSizeClass,
                    onEvent = onEvent,
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                    audioState = state.audioState,
                )
            }
        }
    }

    state.dialogEvent?.let { dialogEvent->
        WordsDetailModalEventsHandler(
            dialogEvent = dialogEvent,
            onEvent = onEvent,
            onNavigateToRelatedWord = onRelatedWordClicked,
            currentPos = 0,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }

    state.sheetEvent?.let { sheetEvent->
        WordsDetailSheetEventsHandler(
            sheetEvent = sheetEvent,
            onEvent = onEvent,
            onSavePointClick = {}
        )
    }
}


@PreviewDesktop
//@Preview(showBackground = true)
@Composable
fun SingleWordDetailNewPagePreview() {
    val word = SampleDatas.generateWordWithSimilar(similarWords = listOf())
    SingleWordDetailPage(
        onRelatedWordClicked = {},
        onEvent = {},
        onNavigateBack = {},
        wordWithSimilar = word,
        state = WordsListDetailState(),
        windowWidthSizeClass = WindowWidthSizeClass.Expanded
    )
}


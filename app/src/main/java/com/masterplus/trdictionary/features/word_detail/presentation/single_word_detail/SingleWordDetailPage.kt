package com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
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
import com.masterplus.trdictionary.core.presentation.features.share.presentation.ShareWordEventHandler
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailEvent
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailState
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.contents.WordsDetailAdaptiveContent
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.handlers.WordsDetailHandleModalEvents
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.handlers.WordsDetailHandleSheetEvents
import com.masterplus.trdictionary.core.util.PreviewDesktop
import com.masterplus.trdictionary.core.util.SampleDatas
import com.masterplus.trdictionary.features.word_detail.domain.model.WordWithSimilar

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
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp),
                    audioState = state.audioState,
                )
            }
        }
    }

    state.dialogEvent?.let { dialogEvent->
        WordsDetailHandleModalEvents(
            dialogEvent = dialogEvent,
            onEvent = onEvent,
            onNavigateToRelatedWord = onRelatedWordClicked,
            currentPos = 0,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }

    state.sheetEvent?.let { sheetEvent->
        WordsDetailHandleSheetEvents(
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


package com.masterplus.trdictionary.features.search.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.ListDetailContentType
import com.masterplus.trdictionary.core.shared_features.share.presentation.ShareWordEventHandler
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailState
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.handlers.WordsDetailModalEventsHandler
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.handlers.WordsDetailSheetEventsHandler
import com.masterplus.trdictionary.core.util.ShowLifecycleToastMessage
import com.masterplus.trdictionary.features.search.presentation.components.SearchFilterDialog
import com.masterplus.trdictionary.features.search.presentation.contents.SearchDetailPageContent
import com.masterplus.trdictionary.features.search.presentation.contents.SearchResultPageContent

@Composable
fun SearchPage(
    onNavigateToBack: ()->Unit,
    searchState: SearchState,
    onSearchEvent: (SearchEvent) -> Unit,
    wordsState: WordsListDetailState,
    onWordsEvent: (WordsListDetailEvent) -> Unit,
    onRelatedWordClicked: (Int) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    listDetailContentType: ListDetailContentType,
    displayFeatures: List<DisplayFeature>
){

    val gridState = rememberLazyGridState()

    ShareWordEventHandler(
        event = wordsState.shareResultEvent,
        onClearEvent = { onWordsEvent(WordsListDetailEvent.ClearShareResult) }
    )

    ShowLifecycleToastMessage(
        message = wordsState.message,
        onDismiss = { onWordsEvent(WordsListDetailEvent.ClearMessage) }
    )

    if(listDetailContentType == ListDetailContentType.DUAL_PANE){
        TwoPane(
            first = {
                SearchResultPageContent(
                    onBackPressed = onNavigateToBack,
                    state = searchState,
                    onEvent = onSearchEvent,
                    isFullPage = false,
                    gridState = gridState
                )
            },
            second = {
                SearchDetailPageContent(
                    onNavigateBack = null,
                    audioState = wordsState.audioState,
                    onEvent = onWordsEvent,
                    wordWithSimilar = searchState.selectedWord,
                    windowWidthSizeClass = windowWidthSizeClass
                )
            },
            strategy = HorizontalTwoPaneStrategy(0.5f, K.twoPaneSpace),
            displayFeatures = displayFeatures
        )
    }else{
        SinglePane(
            searchState = searchState,
            onSearchEvent = onSearchEvent,
            wordsState = wordsState,
            gridState = gridState,
            onWordsEvent = onWordsEvent,
            windowWidthSizeClass = windowWidthSizeClass,
            onNavigateToBack = onNavigateToBack,
            onDetailClose = {
                onSearchEvent(SearchEvent.HideDetail)
            }
        )
    }



    searchState.dialogEvent?.let { dialogEvent->
        ShowDialog(
            state = searchState,
            dialogEvent = dialogEvent,
            onEvent = onSearchEvent
        )
    }

    wordsState.dialogEvent?.let { dialogEvent->
        WordsDetailModalEventsHandler(
            dialogEvent = dialogEvent,
            onEvent = onWordsEvent,
            onNavigateToRelatedWord = onRelatedWordClicked,
            currentPos = 0,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }

    wordsState.sheetEvent?.let { sheetEvent->
        WordsDetailSheetEventsHandler(
            sheetEvent = sheetEvent,
            onEvent = onWordsEvent,
            onSavePointClick = {}
        )
    }


}


@Composable
private fun SinglePane(
    searchState: SearchState,
    onSearchEvent: (SearchEvent) -> Unit,
    wordsState: WordsListDetailState,
    gridState: LazyGridState,
    onWordsEvent: (WordsListDetailEvent) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    onNavigateToBack: ()->Unit,
    onDetailClose: () -> Unit
) {
    val word = searchState.selectedWord
    if(searchState.isDetailOpen && word != null){
        BackHandler {
            onDetailClose()
        }

        SearchDetailPageContent(
            onNavigateBack = onDetailClose,
            audioState = wordsState.audioState,
            onEvent = onWordsEvent,
            wordWithSimilar = searchState.selectedWord,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }else{
        SearchResultPageContent(
            onBackPressed = onNavigateToBack,
            state = searchState,
            onEvent = onSearchEvent,
            isFullPage = true,
            gridState = gridState
        )
    }
}


@Composable
private fun ShowDialog(
    dialogEvent: SearchDialogEvent,
    state: SearchState,
    onEvent: (SearchEvent)->Unit
){
    fun close(){
        onEvent(SearchEvent.ShowDialog(null))
    }

    when(dialogEvent){
        SearchDialogEvent.ShowFilter -> {
            SearchFilterDialog(
                onClosed = ::close,
                onApproved = {categoryEnum, searchKind ->
                    onEvent(SearchEvent.ChangeFilter(categoryEnum,searchKind))
                },
                categoryEnum = state.categoryFilter,
                searchKind = state.searchKind,
                defaultCategoryEnum = state.defaultCategory
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPagePreview() {
    SearchPage(
        onNavigateToBack = {},
        searchState = SearchState(query = TextFieldValue("asd")),
        onSearchEvent = {},
        wordsState = WordsListDetailState(),
        onWordsEvent = {},
        onRelatedWordClicked = {},
        listDetailContentType = ListDetailContentType.SINGLE_PANE,
        displayFeatures = listOf(),
        windowWidthSizeClass = WindowWidthSizeClass.Compact
    )
}


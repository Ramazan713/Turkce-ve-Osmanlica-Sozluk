package com.masterplus.trdictionary.core.presentation.features.word_list_detail.pager

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.masterplus.trdictionary.core.domain.enums.ListDetailContentType
import com.masterplus.trdictionary.features.word_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.features.word_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.handlers.WordsDetailHandleModalEvents
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.handlers.WordsDetailHandleSheetEvents
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailEvent
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailState
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared.WordsDetailTopBarMenu

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordsPagerListDetailAdaptivePage(
    state: WordsListDetailState,
    onEvent: (WordsListDetailEvent) -> Unit,
    words: LazyPagingItems<WordWithSimilar>,
    onListItemLongClick: (Int, WordDetailMeanings) -> Unit,
    onDetailFavoriteClick: (WordDetailMeanings) -> Unit,
    onSavePointClick: (Int?) -> Unit,
    onNavigateBack: (()->Unit)? = null,
    listDetailContentType: ListDetailContentType,
    windowWidthSizeClass: WindowWidthSizeClass,
    displayFeatures: List<DisplayFeature>,
    topBarTitle: String,
    listHeaderDescription: String?,
    initPos: Int = 0
) {

    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = initPos)
    val pagerState = rememberPagerState(initialPage = initPos, pageCount = { words.itemCount })

    WordsPagerPosHandler(
        state = state,
        pagerState = pagerState,
        lazyListState = lazyListState,
        listDetailContentType = listDetailContentType,
        onClearPos = { onEvent(WordsListDetailEvent.ClearNavigateToPos) },
        initPos = initPos
    )


    if(listDetailContentType == ListDetailContentType.DUAL_PANE){
        TwoPane(
            first = {
                WordsPagerListContent(
                    modifier = Modifier.fillMaxSize(),
                    pagingWords = words,
                    lazyListState = lazyListState,
                    onTopBarMenuClick = {
                        handleTopBarMenu(it, onSavePointClick,pagerState.currentPage)
                    },
                    onListItemLongClick = onListItemLongClick,
                    onNavigateBack = onNavigateBack,
                    onEvent = onEvent,
                    title = topBarTitle,
                    listHeaderDescription = listHeaderDescription,
                    selectedPod = pagerState.currentPage
                )
            },
            second = {
                WordsPagerDetailContent(
                    pagingWords = words,
                    pagerState = pagerState,
                    windowWidthSizeClass = windowWidthSizeClass,
                    isFullPage = false,
                    onNavigateBack = {
                        onEvent(WordsListDetailEvent.HideSelectedWords(pagerState.currentPage))
                    },
                    onTopBarMenuClick = {
                        handleTopBarMenu(it, onSavePointClick,pagerState.currentPage)
                    },
                    onEvent = onEvent,
                    onDetailFavoriteClick = onDetailFavoriteClick,
                    audioState = state.audioState,
                    title = topBarTitle,
                )
            },
            HorizontalTwoPaneStrategy(0.5f,12.dp),
            displayFeatures = displayFeatures
        )
    }else{
        SinglePage(
            state = state,
            onEvent = onEvent,
            pagingWords = words,
            windowWidthSizeClass = windowWidthSizeClass,
            pagerState = pagerState,
            lazyListState = lazyListState,
            onNavigateBack = onNavigateBack,
            onListItemLongClick = onListItemLongClick,
            onDetailFavoriteClick = onDetailFavoriteClick,
            onSavePointClick = onSavePointClick,
            topBarTitle = topBarTitle,
            listHeaderDescription = listHeaderDescription,
            onDetailNavigateBack = {
                onEvent(WordsListDetailEvent.HideSelectedWords(pagerState.currentPage))
            }
        )
    }

    state.dialogEvent?.let {dialogEvent->
        WordsDetailHandleModalEvents(
            dialogEvent = dialogEvent,
            onEvent = onEvent,
            currentPos = pagerState.currentPage,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }

    state.sheetEvent?.let { sheetEvent->
        WordsDetailHandleSheetEvents(
            sheetEvent = sheetEvent,
            onEvent = onEvent,
            onSavePointClick = onSavePointClick
        )
    }


}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SinglePage(
    state: WordsListDetailState,
    onEvent: (WordsListDetailEvent) -> Unit,
    pagingWords: LazyPagingItems<WordWithSimilar>,
    windowWidthSizeClass: WindowWidthSizeClass,
    pagerState: PagerState,
    lazyListState: LazyListState,
    onListItemLongClick: (Int,WordDetailMeanings) -> Unit,
    onDetailFavoriteClick: (WordDetailMeanings) -> Unit,
    onSavePointClick: (Int?) -> Unit,
    onNavigateBack: (()->Unit)? = null,
    onDetailNavigateBack: () -> Unit,
    topBarTitle: String,
    listHeaderDescription: String?,
) {
    if(state.isDetailOpen){

        BackHandler {
            onDetailNavigateBack()
        }

        WordsPagerDetailContent(
            pagingWords = pagingWords,
            pagerState = pagerState,
            windowWidthSizeClass = windowWidthSizeClass,
            isFullPage = true,
            onNavigateBack = onDetailNavigateBack,
            onTopBarMenuClick = {
                handleTopBarMenu(it, onSavePointClick,pagerState.currentPage)
            },
            onEvent = onEvent,
            audioState = state.audioState,
            onDetailFavoriteClick = onDetailFavoriteClick,
            title = topBarTitle
        )
    }else{
        WordsPagerListContent(
            modifier = Modifier.fillMaxSize(),
            pagingWords = pagingWords,
            lazyListState = lazyListState,
            onListItemLongClick = onListItemLongClick,
            onTopBarMenuClick = {
                handleTopBarMenu(it, onSavePointClick,pagerState.currentPage)
            },
            onNavigateBack = onNavigateBack,
            onEvent = onEvent,
            title = topBarTitle,
            listHeaderDescription = listHeaderDescription
        )
    }
}

private fun handleTopBarMenu(
    menuItem: WordsDetailTopBarMenu,
    onSavePointClick: (Int?) -> Unit,
    currentPos: Int
){
    when(menuItem){
        WordsDetailTopBarMenu.SavePoint -> {
            onSavePointClick(currentPos)
        }
    }
}

package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.pager

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
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
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.ListDetailContentType
import com.masterplus.trdictionary.core.shared_features.share.presentation.ShareWordEventHandler
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.constants.WordsPagerTopBarMenu
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.handlers.WordsDetailModalEventsHandler
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.handlers.WordsDetailSheetEventsHandler
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailState
import com.masterplus.trdictionary.core.util.ShowLifecycleToastMessage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordsPagerListDetailAdaptivePage(
    state: WordsListDetailState,
    onEvent: (WordsListDetailEvent) -> Unit,
    words: LazyPagingItems<WordWithSimilar>,
    onListItemLongClick: (Int, WordDetailMeanings) -> Unit,
    onDetailFavoriteClick: (WordDetailMeanings) -> Unit,
    onDetailSelectListPressed: (WordDetailMeanings) -> Unit,
    onSavePointClick: (Int?) -> Unit,
    onNavigateToRelatedWord: (Int) -> Unit,
    onNavigateBack: (()->Unit)? = null,
    listDetailContentType: ListDetailContentType,
    windowWidthSizeClass: WindowWidthSizeClass,
    displayFeatures: List<DisplayFeature>,
    topBarTitle: String,
    listHeaderDescription: String?,
    initPos: Int = 0
) {

    val lazyStaggeredState = rememberLazyStaggeredGridState(initialFirstVisibleItemIndex = initPos)
    val pagerState = rememberPagerState(initialPage = initPos, pageCount = { words.itemCount })

    WordsPagerPosHandler(
        state = state,
        pagerState = pagerState,
        lazyStaggeredState = lazyStaggeredState,
        listDetailContentType = listDetailContentType,
        onClearPos = { onEvent(WordsListDetailEvent.ClearNavigateToPos) },
        initPos = { initPos }
    )

    ShareWordEventHandler(
        event = state.shareResultEvent,
        onClearEvent = { onEvent(WordsListDetailEvent.ClearShareResult) }
    )

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = { onEvent(WordsListDetailEvent.ClearMessage) }
    )


    if(listDetailContentType == ListDetailContentType.DUAL_PANE){
        TwoPane(
            first = {
                WordsPagerListContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp),
                    pagingWords = words,
                    lazyStaggeredState = lazyStaggeredState,
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
                    modifier = Modifier.padding(2.dp),
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
                    onDetailSelectListPressed = onDetailSelectListPressed,
                    audioState = state.audioState,
                    title = topBarTitle,
                )
            },
            HorizontalTwoPaneStrategy(0.5f, K.twoPaneSpace),
            displayFeatures = displayFeatures
        )
    }else{
        SinglePage(
            state = state,
            onEvent = onEvent,
            pagingWords = words,
            windowWidthSizeClass = windowWidthSizeClass,
            pagerState = pagerState,
            lazyStaggeredState = lazyStaggeredState,
            onNavigateBack = onNavigateBack,
            onListItemLongClick = onListItemLongClick,
            onDetailFavoriteClick = onDetailFavoriteClick,
            onDetailSelectListPressed = onDetailSelectListPressed,
            onSavePointClick = onSavePointClick,
            topBarTitle = topBarTitle,
            listHeaderDescription = listHeaderDescription,
            onDetailNavigateBack = {
                onEvent(WordsListDetailEvent.HideSelectedWords(pagerState.currentPage))
            }
        )
    }

    state.dialogEvent?.let {dialogEvent->
        WordsDetailModalEventsHandler(
            dialogEvent = dialogEvent,
            onEvent = onEvent,
            currentPos = pagerState.currentPage,
            windowWidthSizeClass = windowWidthSizeClass,
            onNavigateToRelatedWord = onNavigateToRelatedWord
        )
    }

    state.sheetEvent?.let { sheetEvent->
        WordsDetailSheetEventsHandler(
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
    lazyStaggeredState: LazyStaggeredGridState,
    onListItemLongClick: (Int, WordDetailMeanings) -> Unit,
    onDetailFavoriteClick: (WordDetailMeanings) -> Unit,
    onDetailSelectListPressed: (WordDetailMeanings) -> Unit,
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
            modifier = Modifier.padding(2.dp),
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
            onDetailSelectListPressed = onDetailSelectListPressed,
            title = topBarTitle
        )
    }else{
        WordsPagerListContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            pagingWords = pagingWords,
            lazyStaggeredState = lazyStaggeredState,
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
    menuItem: WordsPagerTopBarMenu,
    onSavePointClick: (Int?) -> Unit,
    currentPos: Int
){
    when(menuItem){
        WordsPagerTopBarMenu.SavePoint -> {
            onSavePointClick(currentPos)
        }
    }
}

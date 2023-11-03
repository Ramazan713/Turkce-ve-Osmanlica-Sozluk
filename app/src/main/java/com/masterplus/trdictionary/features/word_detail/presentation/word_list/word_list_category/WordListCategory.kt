package com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.compose.LazyPagingItems
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun WordListCategoryPage(
    pos: Int,
    wordListPos: Int?,
    onNavigateToWordsCategoryDetails: (Int)->Unit,
    onNavigateBack: ()->Unit,
    onItemScrolledNewPos: ()->Unit,
    state: WordListCategoryState,
    sharedState: WordListSharedState,
    onSharedEvent: (WordListSharedEvent)->Unit,
    pagingWords: LazyPagingItems<SimpleWordResult>,
    windowWidthSizeClass: WindowWidthSizeClass,
){

    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = pos)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(wordListPos,lifecycle){
        snapshotFlow { wordListPos }
            .flowWithLifecycle(lifecycle)
            .collectLatest { newPos->
                if (newPos != null) {
                    lazyListState.animateScrollToItem(newPos)
                    onItemScrolledNewPos()
                }
            }
    }

    WordListSharedUiEventManage(
        sharedState = sharedState,
        onSharedEvent = onSharedEvent,
    )

    Scaffold(
        topBar = {
            WordListSharedTopBar(
                title = stringResource(R.string.word_list_c),
                onEvent = {onSharedEvent(it)},
                onNavigateBack = onNavigateBack,
                scrollBehavior = scrollBehavior,
                onSavePointClicked = {
                    onSharedEvent(WordListSharedEvent.ShowDialog(true, WordListSharedDialogEvent.EditSavePoint(
                            state.savePointDestination, state.savePointTitle
                        )
                    ))
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ){paddings->
        WordListContent(
            Modifier.padding(paddings)
                .fillMaxSize()
                .padding(horizontal = 3.dp, vertical = 1.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            pagingWords = pagingWords,
            lazyListState = lazyListState,
            title = state.savePointTitle,
            onClickedWord = {i,_->onNavigateToWordsCategoryDetails(i)},
            onLongClickedWord = {i,word->
                onSharedEvent(WordListSharedEvent.ShowModal(true,
                        WordListSharedModalEvent.ShowSelectBottomMenu(
                            word, i,SavePointType.fromCategory(state.catEnum),state.savePointDestination
                        )
                    )
                )
            }
        )
    }

    if(sharedState.showDialog){
        ShowDialog(
            dialogEvent = sharedState.dialogEvent,
            onEvent = onSharedEvent,
            lazyListState = lazyListState,
            onScrollTo = {scope.launch { lazyListState.animateScrollToItem(it) }},
            windowWidthSizeClass = windowWidthSizeClass
        )
    }

    if(sharedState.showModal){
        ShowModal(
            event = sharedState.modalEvent,
            onEvent = onSharedEvent,
            savePointDestination = state.savePointDestination,
            savePointSubTitle = state.savePointTitle
        )
    }

}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
private fun ShowDialog(
    dialogEvent: WordListSharedDialogEvent?,
    onEvent: (WordListSharedEvent)->Unit,
    lazyListState: LazyListState,
    onScrollTo: (Int)->Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
){
    WordListSharedShowDialog(
        dialogEvent, onEvent, lazyListState, onScrollTo,windowWidthSizeClass
    )
}


@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun ShowModal(
    event: WordListSharedModalEvent?,
    onEvent: (WordListSharedEvent)->Unit,
    savePointDestination: SavePointDestination,
    savePointSubTitle: String
){
    WordListShowModal(
        event = event,
        onEvent = onEvent,
        savePointDestination = savePointDestination,
        savePointSubTitle = savePointSubTitle
    )
}
package com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_for_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.compose.LazyPagingItems
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.model.ListModel
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@OptIn( ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WordListForList(
    pos: Int,
    wordListPos: Int?,
    onNavigateToWordsListDetails: (Int)->Unit,
    onNavigateBack: ()->Unit,
    onItemScrolledNewPos: ()->Unit,
    state: WordListState,
    sharedState: WordListSharedState,
    onSharedEvent: (WordListSharedEvent) -> Unit,
    pagingData: LazyPagingItems<SimpleWordResult>,
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
                title = state.listName,
                onEvent = {onSharedEvent(it)},
                onNavigateBack = onNavigateBack,
                scrollBehavior = scrollBehavior,
                onSavePointClicked = {
                    onSharedEvent(WordListSharedEvent.ShowDialog(
                        true, WordListSharedDialogEvent.EditSavePoint(
                            state.savePointDestination,
                            shortTitle = state.listName
                        )
                    ))
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ){paddings->

        WordListContent(
            modifier = Modifier.padding(paddings)
                .fillMaxSize()
                .padding(horizontal = 3.dp, vertical = 1.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            pagingWords = pagingData,
            lazyListState = lazyListState,
            onClickedWord = {i,_-> onNavigateToWordsListDetails(i)},
            onLongClickedWord = {i,word->
                onSharedEvent(
                    WordListSharedEvent.ShowModal(true,
                        WordListSharedModalEvent.ShowSelectBottomMenu(
                            word, i,SavePointType.List,state.savePointDestination)
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
            onScrollTo = { scope.launch { lazyListState.animateScrollToItem(it) } }
        )
    }

    if(sharedState.showModal){
        ShowModal(
            event = sharedState.modalEvent,
            onEvent = onSharedEvent,
            savePointDestination = state.savePointDestination,
            listModal = state.list
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
    onScrollTo: (Int)->Unit
){
    WordListSharedShowDialog(
        dialogEvent, onEvent, lazyListState, onScrollTo
    )
}


@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun ShowModal(
    event: WordListSharedModalEvent?,
    onEvent: (WordListSharedEvent)->Unit,
    savePointDestination: SavePointDestination,
    listModal: ListModel?
){
    WordListShowModal(
        event = event,
        onEvent = onEvent,
        savePointDestination = savePointDestination,
        savePointSubTitle = listModal?.name?:"",
        listIdControl = listModal?.id
    )
}
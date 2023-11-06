package com.masterplus.trdictionary.core.presentation.features.word_list_detail.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.core.domain.enums.ListDetailContentType
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull

@OptIn(ExperimentalFoundationApi::class, FlowPreview::class)
@Composable
fun WordsPagerPosHandler(
    state: WordsListDetailState,
    pagerState: PagerState,
    lazyListState: LazyListState,
    listDetailContentType: ListDetailContentType,
    onClearPos: () -> Unit,
    initPos: Int
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentOnClearPos by rememberUpdatedState(newValue = onClearPos)
    val currentPagerState by rememberUpdatedState(newValue = pagerState)
    val currentLazyListState by rememberUpdatedState(newValue = lazyListState)

    var currentPos by rememberSaveable {
        mutableIntStateOf(initPos)
    }
    var posForRefresh by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(state.selectedDetailPos,lifecycleOwner){
        snapshotFlow { state.selectedDetailPos }
            .filterNotNull()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { pos->
                currentOnClearPos()
                currentPagerState.scrollToPage(pos)
            }
    }

    LaunchedEffect(state.navigateToListPos,lifecycleOwner){
        snapshotFlow { state.navigateToListPos }
            .filterNotNull()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { pos->
                currentOnClearPos()
                currentLazyListState.scrollToItem(pos)
            }
    }

    LaunchedEffect(state.navigateToPos,lifecycleOwner){
        snapshotFlow { state.navigateToPos }
            .filterNotNull()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { pos->
                posForRefresh = true
                currentPos = pos
                currentOnClearPos()
            }
    }

    LaunchedEffect(currentPos,lifecycleOwner,listDetailContentType){
        snapshotFlow { currentPos }
            .distinctUntilChanged()
            .filter { listDetailContentType == ListDetailContentType.DUAL_PANE }
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { pos->
                if(posForRefresh){
                    posForRefresh = false
                    currentLazyListState.scrollToItem(pos)
                    currentPagerState.scrollToPage(pos)
                }else{
                    currentPagerState.animateScrollToPage(pos)
                    currentLazyListState.animateScrollToItem(pos)
                }
            }
    }

    LaunchedEffect(currentPos,lifecycleOwner,listDetailContentType){
        snapshotFlow { currentPos }
            .filter { listDetailContentType == ListDetailContentType.SINGLE_PANE }
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { pos->
                if(state.isDetailOpen){
                    currentPagerState.animateScrollToPage(pos)
                }else{
                    currentLazyListState.animateScrollToItem(pos)
                }
            }
    }

    LaunchedEffect(pagerState,lifecycleOwner){
        snapshotFlow { pagerState.currentPage }
            .debounce(300)
            .distinctUntilChanged()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest {pos->
                currentPos = pos
            }
    }

    LaunchedEffect(lazyListState,lifecycleOwner){
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .debounce(300)
            .distinctUntilChanged()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest {pos->
                currentPos = pos
            }
    }


}
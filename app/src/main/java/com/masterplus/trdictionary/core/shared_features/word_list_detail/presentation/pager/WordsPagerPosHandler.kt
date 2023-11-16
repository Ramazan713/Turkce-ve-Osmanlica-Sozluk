package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.pager

import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
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
import com.masterplus.trdictionary.core.extensions.isNumberInRange
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.parcelize.Parcelize

private suspend fun LazyStaggeredGridState.customScrollToPos(pos: Int, animate: Boolean = true){
    if(animate){
        animateScrollToItem(pos)
    }else{
        scrollToItem(pos)
    }
}

@OptIn(ExperimentalFoundationApi::class)
private suspend fun PagerState.customScrollToPos(pos: Int, animate: Boolean = true){
    if(animate){
        animateScrollToPage(pos)
    }else{
        scrollToPage(pos)
    }
}




@OptIn(ExperimentalFoundationApi::class, FlowPreview::class)
@Composable
fun WordsPagerPosHandler(
    state: WordsListDetailState,
    pagerState: PagerState,
    lazyStaggeredState: LazyStaggeredGridState,
    listDetailContentType: ListDetailContentType,
    onClearPos: () -> Unit,
    initPos: () -> Int
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentOnClearPos by rememberUpdatedState(newValue = onClearPos)
    val currentPagerState by rememberUpdatedState(newValue = pagerState)
    val currentLazyStaggeredState by rememberUpdatedState(newValue = lazyStaggeredState)

    var currentPos by rememberSaveable {
        mutableStateOf(CurrentPos(initPos(),scrollTo = false))
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
                currentLazyStaggeredState.scrollToItem(pos)
            }
    }

    LaunchedEffect(state.navigateToPos,lifecycleOwner){
        snapshotFlow { state.navigateToPos }
            .filterNotNull()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { pos->
                posForRefresh = true
                currentPos = CurrentPos(pos)
                currentOnClearPos()
            }
    }

    LaunchedEffect(currentPos,lifecycleOwner,listDetailContentType){
        snapshotFlow { currentPos }
            .distinctUntilChanged()
            .filter { it.scrollTo }
            .filter { listDetailContentType == ListDetailContentType.DUAL_PANE }
            .map { it.pos }
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { pos->
                currentLazyStaggeredState.customScrollToPos(pos, animate = !posForRefresh)
                currentPagerState.customScrollToPos(pos, animate = !posForRefresh)
                if(posForRefresh){
                    posForRefresh = false
                }
            }
    }

    LaunchedEffect(currentPos,lifecycleOwner,listDetailContentType){
        snapshotFlow { currentPos }
            .distinctUntilChanged()
            .filter { it.scrollTo }
            .filter { listDetailContentType == ListDetailContentType.SINGLE_PANE }
            .map { it.pos }
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { pos->
                if(state.isDetailOpen){
                    currentPagerState.customScrollToPos(pos, animate = !posForRefresh)
                }else{
                    currentLazyStaggeredState.customScrollToPos(pos, animate = !posForRefresh)
                }
                if(posForRefresh){
                    posForRefresh = false
                }
            }
    }

    LaunchedEffect(pagerState,lifecycleOwner){
        snapshotFlow { pagerState.currentPage }
            .debounce(300)
            .filter { state.isDetailOpen || listDetailContentType == ListDetailContentType.DUAL_PANE }
            .distinctUntilChanged()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest {pos->
                currentPos = CurrentPos(pos,false)
                currentLazyStaggeredState.animateScrollToItem(pos)
            }
    }


    LaunchedEffect(lazyStaggeredState,lifecycleOwner){
        snapshotFlow { lazyStaggeredState.layoutInfo }
            .debounce(500)
            .filter { it.isNumberInRange(currentPos.pos) == false }
            .filter { !state.isDetailOpen || listDetailContentType == ListDetailContentType.DUAL_PANE }
            .map { it.visibleItemsInfo.firstOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest {pos->
                currentPos = CurrentPos(pos,false)
                currentPagerState.animateScrollToPage(pos)
            }
    }
}


@Parcelize
private data class CurrentPos(
    val pos: Int,
    val scrollTo: Boolean = true
): Parcelable

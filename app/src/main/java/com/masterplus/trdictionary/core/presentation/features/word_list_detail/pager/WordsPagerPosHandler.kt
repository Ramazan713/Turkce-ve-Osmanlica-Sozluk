package com.masterplus.trdictionary.core.presentation.features.word_list_detail.pager

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.grid.LazyGridLayoutInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
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
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalFoundationApi::class, FlowPreview::class)
@Composable
fun WordsPagerPosHandler(
    state: WordsListDetailState,
    pagerState: PagerState,
    lazyGridState: LazyGridState,
    listDetailContentType: ListDetailContentType,
    onClearPos: () -> Unit,
    initPos: () -> Int
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentOnClearPos by rememberUpdatedState(newValue = onClearPos)
    val currentPagerState by rememberUpdatedState(newValue = pagerState)
    val currentLazyGridState by rememberUpdatedState(newValue = lazyGridState)

    var currentPos by rememberSaveable {
        mutableIntStateOf(initPos())
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
                currentLazyGridState.scrollToItem(pos)
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
                    currentLazyGridState.scrollToItem(pos)
                    currentPagerState.scrollToPage(pos)
                }else{
                    currentPagerState.animateScrollToPage(pos)
                    currentLazyGridState.animateScrollToItem(pos)
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
                    currentLazyGridState.animateScrollToItem(pos)
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


    LaunchedEffect(lazyGridState,lifecycleOwner){
        snapshotFlow { lazyGridState.layoutInfo }
            .debounce(500)
            .filter { it.isNumberInRange(currentPos) == false }
            .map { it.visibleItemsInfo.firstOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest {pos->
                currentPos = pos
            }
    }
}

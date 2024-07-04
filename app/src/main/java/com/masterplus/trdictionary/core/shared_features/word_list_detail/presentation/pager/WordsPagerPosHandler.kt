package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.pager

import android.os.Parcelable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.core.domain.enums.ListDetailContentType
import com.masterplus.trdictionary.core.extensions.isNumberInRange
import com.masterplus.trdictionary.core.presentation.utils.EventHandler
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.parcelize.Parcelize

private suspend fun LazyStaggeredGridState.customScrollToPos(pos: Int, animate: Boolean = true){
    if(animate){
        animateScrollToItem(pos)
    }else{
        scrollToItem(pos)
    }
}

private suspend fun PagerState.customScrollToPos(pos: Int, animate: Boolean = true){
    if(animate){
        animateScrollToPage(pos)
    }else{
        scrollToPage(pos)
    }
}




@OptIn(FlowPreview::class)
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

    //current pager not active, when it is active then value set by currentValue
    var isPagerPosNotSetByList by rememberSaveable {
        mutableStateOf(false)
    }

    //current list not active, when it is active then value set by currentValue
    var isListPosNotSetByPager by rememberSaveable {
        mutableStateOf(false)
    }

    EventHandler(event = state.selectedDetailPos) { pos->
        currentOnClearPos()
        currentPagerState.scrollToPage(pos)
    }

    EventHandler(event = state.navigateToListPos) { pos->
        currentOnClearPos()
        currentPos = CurrentPos(pos,false)
        currentLazyStaggeredState.scrollToItem(pos)
    }

    EventHandler(event = state.navigateToPos) { pos->
        posForRefresh = true
        currentPos = CurrentPos(pos)
        currentOnClearPos()
    }


    LaunchedEffect(currentPos,lifecycleOwner.lifecycle,listDetailContentType,state.isDetailOpen){
        snapshotFlow { currentPos }
            .distinctUntilChanged()
            .filter { it.scrollTo }
            .map { it.pos }
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { pos->
                if(listDetailContentType == ListDetailContentType.SINGLE_PANE){
                    if(state.isDetailOpen){
                        isListPosNotSetByPager = true
                        currentPagerState.customScrollToPos(pos, animate = !posForRefresh)
                    }else{
                        isPagerPosNotSetByList = true
                        currentLazyStaggeredState.customScrollToPos(pos, animate = !posForRefresh)
                    }
                }else{
                    currentLazyStaggeredState.customScrollToPos(pos, animate = !posForRefresh)
                    currentPagerState.customScrollToPos(pos, animate = !posForRefresh)
                }

                if(posForRefresh){
                    posForRefresh = false
                }
            }
    }

    LaunchedEffect(pagerState, lifecycleOwner.lifecycle, listDetailContentType){
        snapshotFlow { pagerState.currentPage }
            .debounce(300)
            .filter { state.isDetailOpen || listDetailContentType == ListDetailContentType.DUAL_PANE }
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .distinctUntilChanged()
            .collectLatest {pos->
                if(isPagerPosNotSetByList){
                    isPagerPosNotSetByList = false
                    pagerState.animateScrollToPage(currentPos.pos)
                }else{
                    if(listDetailContentType == ListDetailContentType.DUAL_PANE){
                        currentPos = CurrentPos(pos,false)
                        currentLazyStaggeredState.animateScrollToItem(pos)
                    }else{
                        isListPosNotSetByPager = true
                    }
                }
            }
    }


    LaunchedEffect(lazyStaggeredState,lifecycleOwner.lifecycle, listDetailContentType){

        snapshotFlow { lazyStaggeredState.layoutInfo }
            .debounce(500)
            .filter { it.isNumberInRange(currentPos.pos) == false }
            .filter { !state.isDetailOpen || listDetailContentType == ListDetailContentType.DUAL_PANE }
            .map { it.visibleItemsInfo.firstOrNull()?.index ?: 0 }
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .distinctUntilChanged()
            .collectLatest {pos->
                if(listDetailContentType == ListDetailContentType.DUAL_PANE){
                    if(isListPosNotSetByPager){
                        isListPosNotSetByPager = false
                        lazyStaggeredState.scrollToItem(currentPos.pos)
                    }else{
                        currentPos = CurrentPos(pos,false)
                        currentPagerState.animateScrollToPage(pos)
                    }
                }else{
                    isPagerPosNotSetByList = true
                }
            }
    }

}


@Parcelize
private data class CurrentPos(
    val pos: Int,
    val scrollTo: Boolean = true
): Parcelable

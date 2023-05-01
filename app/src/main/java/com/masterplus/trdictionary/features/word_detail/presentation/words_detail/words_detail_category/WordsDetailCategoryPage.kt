package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_category

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.presentation.components.CustomModalBottomSheet
import com.masterplus.trdictionary.core.presentation.features.select_list.select_list_dia.SelectListBottomContent
import com.masterplus.trdictionary.features.word_detail.domain.model.WordCompletedInfo
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@Composable
fun WordsDetailCategoryPage(
    pos: Int = 0,
    onNavigateBack: (Int)->Unit,
    onRelatedWordClicked: (Int)->Unit,
    state: WordsDetailCategoryState,
    sharedState: WordsDetailSharedState,
    onSharedEvent: (WordsDetailSharedEvent)->Unit,
    pagingWords: LazyPagingItems<WordCompletedInfo>
){

    val pagerState = rememberPagerState(initialPage = pos)
    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    WordListSharedUiEventManage(
        sharedState = sharedState,
        onSharedEvent = onSharedEvent
    )

    BackHandler {
        onNavigateBack(pagerState.currentPage)
    }

    Scaffold(
        topBar = {
            WordsDetailSharedTopBar(
                title = stringResource(R.string.word_list_c),
                onEvent = {onSharedEvent(it)},
                scrollBehavior = scrollBehavior,
                onNavigateBack = {onNavigateBack(pagerState.currentPage)},
                onSavePointClicked = {
                    onSharedEvent(WordsDetailSharedEvent.ShowDialog(
                        true, WordsDetailSharedDialogEvent.EditSavePoint(
                            state.savePointDestination, state.savePointTitle
                        )
                    ))
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {paddings->
        WordsDetailContent(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            pagingWords = pagingWords,
            pagerState = pagerState,
            onEvent = onSharedEvent,
            onFavoriteClicked = {_,word->
                onSharedEvent(WordsDetailSharedEvent.AddFavorite(word.wordId))
            },
            audioState = state.audioState,
            onShareMenuItemClicked = {_,wordId,wordRandomOrder,sharedItem->
                onSharedEvent(WordsDetailSharedEvent.EvaluateShareWord(
                    state.savePointDestination,SavePointType.fromCategory(state.catEnum),
                    pagerState.currentPage,wordId,wordRandomOrder,sharedItem
                ))
            }
        )
    }

    if(sharedState.showDialog){
        ShowDialog(
            event = sharedState.dialogEvent,
            onEvent = onSharedEvent,
            onNavigateToDetailWord = onRelatedWordClicked,
            onScrollTo = { scope.launch { pagerState.animateScrollToPage(it) } },
            currentPage = pagerState.currentPage,
            maxPages = pagingWords.itemCount
        )
    }

    if(sharedState.showModal){
        ShowModal(
            event = sharedState.modalEvent,
            onEvent = onSharedEvent
        )
    }

}

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
private fun ShowDialog(
    event: WordsDetailSharedDialogEvent?,
    onEvent: (WordsDetailSharedEvent)->Unit,
    onNavigateToDetailWord: (Int)->Unit,
    currentPage: Int,
    maxPages: Int,
    onScrollTo: (Int)->Unit
){
    WordsDetailShowSharedDialog(
        event = event,
        onEvent = onEvent,
        onNavigateToDetailWord = onNavigateToDetailWord,
        currentPage = currentPage,
        maxPages = maxPages,
        onScrollTo = onScrollTo
    )
}


@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
private fun ShowModal(
    event: WordsDetailSharedModalEvent?,
    onEvent: (WordsDetailSharedEvent)->Unit,
){
    fun close(){
        onEvent(WordsDetailSharedEvent.ShowModal(false))
    }

    CustomModalBottomSheet(
        onDismissRequest = {close()},
        skipHalfExpanded = false
    ){
        when(event){
            is WordsDetailSharedModalEvent.ShowSelectList -> {
                SelectListBottomContent(
                    wordId = event.wordId,
                    onClosed = {close()}
                )
            }
            null -> {}
        }
    }
}






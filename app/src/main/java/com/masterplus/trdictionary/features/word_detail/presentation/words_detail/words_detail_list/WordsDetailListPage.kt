package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.core.presentation.features.select_list.select_list_dia.SelectListBottomContent
import com.masterplus.trdictionary.features.word_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.features.word_detail.domain.model.WordWithSimilarRelationModel
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@ExperimentalCoroutinesApi
@Composable
fun WordsDetailListPage(
    pos: Int = 0,
    onNavigateBack: (Int)->Unit,
    onRelatedWordClicked: (Int)->Unit,
    state: WordsDetailListState,
    onEvent: (WordsDetailListEvent)->Unit,
    sharedState: WordsDetailSharedState,
    onSharedEvent: (WordsDetailSharedEvent)->Unit,
    pagingWords: LazyPagingItems<WordWithSimilar>,
    windowWidthSizeClass: WindowWidthSizeClass,
){

    val pagerState = rememberPagerState(initialPage = pos, pageCount = { pagingWords.itemCount })
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
                title = state.listName,
                onEvent = onSharedEvent,
                scrollBehavior = scrollBehavior,
                onNavigateBack = {onNavigateBack(pagerState.currentPage)},
                onSavePointClicked = {
                    onSharedEvent(WordsDetailSharedEvent.ShowDialog(
                            true, WordsDetailSharedDialogEvent.EditSavePoint(
                                state.savePointDestination, state.listName
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
            onFavoriteClicked = { _, word->
                onEvent(WordsDetailListEvent.AddOrAskFavorite(word.wordId))
            },
            audioState = state.audioState,
            onShareMenuItemClicked = {index,wordId,wordRandomOrder,sharedItem->
                onSharedEvent(WordsDetailSharedEvent.EvaluateShareWord(
                    state.savePointDestination,SavePointType.List,index,wordId,wordRandomOrder,sharedItem
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
            maxPages = pagingWords.itemCount,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }

    if(sharedState.showModal){
        ShowModal(
            event = sharedState.modalEvent,
            onEvent = onSharedEvent,
            listId = state.listModel?.id?:0
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
    onScrollTo: (Int)->Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
){

    fun close(){
        onEvent(WordsDetailSharedEvent.ShowDialog(false))
    }

    when(event){
        is WordDetailListDialogEvent.AskUnFavorite -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_remove_list_from_favorite),
                content = stringResource(R.string.affect_current_list),
                onClosed = {close()},
                onApproved = {
                    onEvent(WordsDetailSharedEvent.AddFavorite(event.wordId))
                }
            )
        }
        else->{
            WordsDetailShowSharedDialog(
                event = event,
                onEvent = onEvent,
                onNavigateToDetailWord = onNavigateToDetailWord,
                currentPage = currentPage,
                maxPages = maxPages,
                onScrollTo = onScrollTo,
                windowWidthSizeClass = windowWidthSizeClass
            )
        }
    }
}


@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
private fun ShowModal(
    event: WordsDetailSharedModalEvent?,
    listId: Int,
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
                    listIdControl = listId,
                    onClosed = {close()}
                )
            }
            null -> {}
        }
    }
}

package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_list.navigation

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_list.WordsDetailListPage
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_list.WordsDetailListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


const val RouteWordsDetailList = "wordsDetailList/{listId}/{pos}"


data class WordsDetailListArgs(val listId: Int, val pos: Int){
    constructor(savedStateHandle: SavedStateHandle): this(
        checkNotNull(savedStateHandle["listId"]) as Int,
        checkNotNull(savedStateHandle["pos"]) as Int,
    )
}


fun NavController.navigateToWordsDetailList(listId: Int, pos: Int = 0){
    navigate("wordsDetailList/${listId}/${pos}")
}


@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
fun NavGraphBuilder.wordsDetailList(
    onNavigateBack: (Int)->Unit,
    onRelatedWordClicked: (Int)->Unit,
){
    composable(
        RouteWordsDetailList,
        arguments = listOf(
            navArgument("listId"){type = NavType.IntType},
            navArgument("pos"){type = NavType.IntType},
        )
    ){
        val wordsDetailListViewModel: WordsDetailListViewModel = hiltViewModel()
        val args = wordsDetailListViewModel.args
        val pagingWords = wordsDetailListViewModel.pagingWords.collectAsLazyPagingItems()

        WordsDetailListPage(
            pos = args.pos,
            state = wordsDetailListViewModel.state,
            onEvent = wordsDetailListViewModel::onEvent,
            sharedState = wordsDetailListViewModel.sharedState,
            onSharedEvent = wordsDetailListViewModel::onSharedEvent,
            pagingWords = pagingWords,
            onNavigateBack = onNavigateBack,
            onRelatedWordClicked = onRelatedWordClicked
        )
    }
}
package com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_for_list.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_for_list.WordListForList
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_for_list.WordListForListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

const val RouteWordListForList = "wordListForList/{listId}/{pos}"


data class WordListForListArgs(val listId: Int, val pos: Int){
    constructor(savedStateHandle: SavedStateHandle): this(
        checkNotNull(savedStateHandle["listId"]) as Int,
        checkNotNull(savedStateHandle["pos"]) as Int,
    )
}


fun NavController.navigateToWordListForList(listId: Int, pos: Int = 0){
    navigate("wordListForList/${listId}/${pos}")
}

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalComposeUiApi
fun NavGraphBuilder.wordListForList(
    onNavigateToWordsListDetails: (Int,Int)->Unit,
    onNavigateBack: ()->Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
){
    composable(
        RouteWordListForList,
        arguments = listOf(
            navArgument("listId"){type = NavType.IntType},
            navArgument("pos"){type = NavType.IntType},
        )
    ){stackEntry->
        val lastPos = stackEntry.savedStateHandle.get<Int?>("wordsDetailLastPos")
        val wordListViewModel: WordListForListViewModel = hiltViewModel()
        val args = wordListViewModel.args

        val pagingData = wordListViewModel.pagingWords.collectAsLazyPagingItems()
        val state = wordListViewModel.state
        val sharedState = wordListViewModel.sharedState

        WordListForList(
            pos = args.pos,
            wordListPos = lastPos,
            onNavigateToWordsListDetails = { newPos->
                onNavigateToWordsListDetails(args.listId,newPos)
            },
            onNavigateBack = onNavigateBack,
            state = state,
            sharedState = sharedState,
            pagingData = pagingData,
            onSharedEvent = wordListViewModel::onSharedEvent,
            onItemScrolledNewPos = {
                stackEntry.savedStateHandle.remove<Int>("wordsDetailLastPos")
            },
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
}

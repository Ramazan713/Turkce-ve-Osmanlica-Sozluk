package com.masterplus.trdictionary.features.word_detail.word_list_for_list_detail.navigation

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
import androidx.window.layout.DisplayFeature
import com.masterplus.trdictionary.core.domain.enums.ListDetailContentType
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailViewModel
import com.masterplus.trdictionary.features.word_detail.word_list_for_list_detail.WordListForListDetailPage
import com.masterplus.trdictionary.features.word_detail.word_list_for_list_detail.WordListForListDetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


const val RouteWordListForListDetail = "wordListForListDetail/{listId}/{pos}"


data class WordListForListDetailArgs(val listId: Int, val pos: Int){
    constructor(savedStateHandle: SavedStateHandle): this(
        checkNotNull(savedStateHandle["listId"]) as Int,
        checkNotNull(savedStateHandle["pos"]) as Int,
    )
}


fun NavController.navigateToWordListForListDetail(listId: Int, pos: Int = 0){
    navigate("wordListForListDetail/${listId}/${pos}")
}

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalComposeUiApi
fun NavGraphBuilder.wordListForListDetail(
    onRelatedWordClicked: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    listDetailContentType: ListDetailContentType,
    displayFeatures: List<DisplayFeature>
){
    composable(
        RouteWordListForListDetail,
        arguments = listOf(
            navArgument("listId"){type = NavType.IntType},
            navArgument("pos"){type = NavType.IntType},
        )
    ){
        val wordListViewModel = hiltViewModel<WordListForListDetailViewModel>()
        val listDetailViewModel = hiltViewModel<WordsListDetailViewModel>()

        val args = wordListViewModel.args

        val words = wordListViewModel.pagingWords.collectAsLazyPagingItems()

        WordListForListDetailPage(
            state = listDetailViewModel.state,
            onEvent = listDetailViewModel::onEvent,
            listDetailContentType = listDetailContentType,
            windowWidthSizeClass = windowWidthSizeClass,
            displayFeatures = displayFeatures,
            words = words,
            onNavigateBack = onNavigateBack,
            initPos = args.pos,
            onRelatedWordClicked = onRelatedWordClicked,
            listId = args.listId,
            listName = wordListViewModel.listName,
            savePointDestination = wordListViewModel.savePointDestination
        )
    }
}

package com.masterplus.trdictionary.features.search.presentation.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.window.layout.DisplayFeature
import com.masterplus.trdictionary.core.domain.enums.ListDetailContentType
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailViewModel
import com.masterplus.trdictionary.features.search.presentation.SearchPage
import com.masterplus.trdictionary.features.search.presentation.SearchViewModel

private const val routeName = "search/{catId}"

class SearchArgs(val catId: Int) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(checkNotNull(savedStateHandle["catId"]) as Int)
}

fun NavController.navigateToSearch(catId: Int){
    this.navigate("search/$catId")
}

fun NavGraphBuilder.searchPage(
    onBackPressed: () -> Unit,
    onRelatedWordClicked: (Int) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    listDetailContentType: ListDetailContentType,
    displayFeatures: List<DisplayFeature>
){
    composable(
        routeName,
        arguments = listOf(
            navArgument("catId"){type = NavType.IntType},
        )
    ){
        val searchViewModel = hiltViewModel<SearchViewModel>()
        val sharedWordDetailViewModel = hiltViewModel<WordsListDetailViewModel>()

        SearchPage(
            onNavigateToBack = onBackPressed,
            searchState = searchViewModel.state,
            onSearchEvent = searchViewModel::onEvent,
            wordsState = sharedWordDetailViewModel.state,
            onWordsEvent = sharedWordDetailViewModel::onEvent,
            onRelatedWordClicked = onRelatedWordClicked,
            listDetailContentType = listDetailContentType,
            displayFeatures = displayFeatures,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
}

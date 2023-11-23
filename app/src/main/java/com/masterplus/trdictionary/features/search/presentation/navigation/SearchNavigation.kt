package com.masterplus.trdictionary.features.search.presentation.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

const val RouteSearch = "search/{catId}"

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
        RouteSearch,
        arguments = listOf(
            navArgument("catId"){type = NavType.IntType},
        )
    ){
        val searchViewModel = hiltViewModel<SearchViewModel>()
        val sharedWordDetailViewModel = hiltViewModel<WordsListDetailViewModel>()

        val searchState by searchViewModel.state.collectAsStateWithLifecycle()
        val wordsState by sharedWordDetailViewModel.state.collectAsStateWithLifecycle()

        SearchPage(
            onNavigateToBack = onBackPressed,
            searchState = searchState,
            onSearchEvent = searchViewModel::onEvent,
            wordsState = wordsState,
            onWordsEvent = sharedWordDetailViewModel::onEvent,
            onRelatedWordClicked = onRelatedWordClicked,
            listDetailContentType = listDetailContentType,
            displayFeatures = displayFeatures,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
}

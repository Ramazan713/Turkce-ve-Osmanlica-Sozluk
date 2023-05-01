package com.masterplus.trdictionary.features.search.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
    onBackPressed: ()->Unit,
    onNavigateToWordDetail: (Int,Boolean)->Unit,
){
    composable(
        routeName,
        arguments = listOf(
            navArgument("catId"){type = NavType.IntType},
        )
    ){
        val searchViewModel: SearchViewModel = hiltViewModel()

        SearchPage(
            onBackPressed = onBackPressed,
            onNavigateToWordDetail = onNavigateToWordDetail,
            state = searchViewModel.state,
            onEvent = searchViewModel::onEvent
        )
    }
}

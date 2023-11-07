package com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail.navigation

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailViewModel
import com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail.SingleWordDetailPage
import com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail.SingleWordDetailViewModel

const val RouteSingleWordDetail = "singleWordDetail/{wordId}"


class SingleWordDetailArgs(val wordId: Int) {
    constructor(savedStateHandle: SavedStateHandle) :
            this((savedStateHandle.get<String?>("wordId"))?.toIntOrNull()?:1)
}


fun NavController.navigateToSingleWordDetail(wordId: Int,navOptions: NavOptions? = null){
    this.navigate("singleWordDetail/$wordId", navOptions = navOptions)
}


@ExperimentalFoundationApi
fun NavGraphBuilder.singleWordDetail(
    onNavigateBack: ()->Unit,
    onRelatedWordClicked: (Int)->Unit,
    windowWidthSizeClass: WindowWidthSizeClass
){
    composable(
        RouteSingleWordDetail,
        arguments = listOf(
            navArgument("wordId"){ type = NavType.StringType }
        ),
    ){
        val sharedViewModel = hiltViewModel<WordsListDetailViewModel>()
        val wordDetailViewModel = hiltViewModel<SingleWordDetailViewModel>()

        val word by wordDetailViewModel.word.collectAsStateWithLifecycle(initialValue = null)

        SingleWordDetailPage(
            onNavigateBack = onNavigateBack,
            onRelatedWordClicked = onRelatedWordClicked,
            state = sharedViewModel.state,
            onEvent = sharedViewModel::onEvent,
            wordWithSimilar = word,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
}
package com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail.navigation

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.masterplus.trdictionary.core.domain.constants.K
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
        deepLinks = listOf(
            navDeepLink {
                action = Intent.ACTION_VIEW
                uriPattern = "${K.DeepLink.singleWordBaseUrl}/{wordId}"

            },
        )
    ){
        val wordDetailViewModel: SingleWordDetailViewModel = hiltViewModel()

        SingleWordDetailPage(
            onNavigateBack = onNavigateBack,
            onRelatedWordClicked = onRelatedWordClicked,
            state = wordDetailViewModel.state,
            onEvent = wordDetailViewModel::onEvent,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
}
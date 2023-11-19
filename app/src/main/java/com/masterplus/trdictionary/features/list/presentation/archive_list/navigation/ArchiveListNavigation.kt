package com.masterplus.trdictionary.features.list.presentation.archive_list.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.trdictionary.features.list.presentation.archive_list.ArchiveListPage
import com.masterplus.trdictionary.features.list.presentation.archive_list.ArchiveListViewModel


private const val routeName = "archiveList"

fun NavController.navigateToArchiveList(){
    this.navigate(routeName)
}

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
fun NavGraphBuilder.archiveListPage(
    onNavigateBack: ()->Unit,
    onNavigateToDetailList: (listId: Int)->Unit,
    windowWidthSizeClass: WindowWidthSizeClass
){
    composable(routeName){
        val listViewModel: ArchiveListViewModel = hiltViewModel()

        val state by listViewModel.state.collectAsStateWithLifecycle()

        ArchiveListPage(
            onNavigateBack = onNavigateBack,
            onNavigateToDetailList = onNavigateToDetailList,
            state = state,
            onEvent = listViewModel::onEvent,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
}
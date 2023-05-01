package com.masterplus.trdictionary.features.list.presentation.archive_list.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
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
){
    composable(routeName){

        val listViewModel: ArchiveListViewModel = hiltViewModel()

        ArchiveListPage(
            onNavigateBack = onNavigateBack,
            onNavigateToDetailList = onNavigateToDetailList,
            state = listViewModel.state,
            onEvent = listViewModel::onEvent
        )
    }
}
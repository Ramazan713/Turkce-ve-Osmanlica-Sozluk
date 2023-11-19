package com.masterplus.trdictionary.features.list.presentation.show_list.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.trdictionary.features.list.presentation.show_list.ShowListPage
import com.masterplus.trdictionary.features.list.presentation.show_list.ShowListViewModel


const val RouteList = "ShowList"

@ExperimentalMaterial3Api
fun NavGraphBuilder.showListPage(
    onNavigateToArchive: ()->Unit,
    onNavigateToDetailList: (listId: Int)->Unit,
    onNavigateToSelectSavePoint: (String,List<Int>,Int)->Unit,
    onNavigateToSettings: ()->Unit,
    windowWidthSizeClass: WindowWidthSizeClass
){
    composable(RouteList){
        val listViewModel: ShowListViewModel = hiltViewModel()

        val state by listViewModel.state.collectAsStateWithLifecycle()

        ShowListPage(
            onNavigateToDetailList = onNavigateToDetailList,
            onNavigateToSelectSavePoint = onNavigateToSelectSavePoint,
            onNavigateToArchive = onNavigateToArchive,
            onNavigateToSettings = onNavigateToSettings,
            state = state,
            onEvent = listViewModel::onEvent,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
}
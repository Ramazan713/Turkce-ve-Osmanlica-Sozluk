package com.masterplus.trdictionary.features.list.presentation.show_list.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.hilt.navigation.compose.hiltViewModel
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
){
    composable(RouteList){

        val listViewModel: ShowListViewModel = hiltViewModel()

        ShowListPage(
            onNavigateToDetailList = onNavigateToDetailList,
            onNavigateToSelectSavePoint = onNavigateToSelectSavePoint,
            onNavigateToArchive = onNavigateToArchive,
            onNavigateToSettings = onNavigateToSettings,
            state = listViewModel.state,
            onEvent = listViewModel::onEvent
        )
    }
}
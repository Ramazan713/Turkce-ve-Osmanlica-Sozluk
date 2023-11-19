package com.masterplus.trdictionary.features.savepoint.presentation.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.features.savepoint.presentation.SelectSavePointPage
import com.masterplus.trdictionary.features.savepoint.presentation.SelectSavePointViewModel

private const val routeName = "selectSavePoint/{title}/{typeId}/{destinationFilters}"


class SelectSavePointArgs(val typeId: Int, val title: String, val destinationFilters: List<Int>) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        checkNotNull(savedStateHandle["typeId"]),
        checkNotNull(savedStateHandle["title"]),
        checkNotNull(savedStateHandle.get<String>("destinationFilters")?:"").let { destinationFiltersStr->
            val type = TypeToken.get(Array<Int>::class.java).type
            val destinationFilters = Gson().fromJson<Array<Int>>(destinationFiltersStr,type)
            destinationFilters.toList()
        }
    )
}


fun NavController.navigateToSelectSavePoint(typeId: Int, destinationFilters: List<Int>,title: String){
    val encodedArray = Gson().toJson(destinationFilters.toTypedArray())
    this.navigate("selectSavePoint/$title/$typeId/$encodedArray")
}


@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
fun NavGraphBuilder.selectSavePoint(
    onNavigateBack: ()->Unit,
    onNavigateToList: (listId: Int,pos: Int)->Unit,
    onNavigateToCatRandom: (CategoryEnum, Int)->Unit,
    onNavigateToCatAll: (CategoryEnum, Int)->Unit,
    onNavigateToCatAlphabetic: (CategoryEnum, String, Int)->Unit,
){
    composable(
        routeName,
        arguments = listOf(
            navArgument("destinationFilters"){type = NavType.StringType},
            navArgument("title"){type = NavType.StringType},
            navArgument("typeId"){type = NavType.IntType}
        )
    ){
        val savePointViewModel: SelectSavePointViewModel = hiltViewModel()

        val state by savePointViewModel.state.collectAsStateWithLifecycle()

        SelectSavePointPage(
            onNavigateBack = onNavigateBack,
            onNavigateToList = onNavigateToList,
            onNavigateToCatAll = onNavigateToCatAll,
            onNavigateToCatRandom = onNavigateToCatRandom,
            onNavigateToCatAlphabetic = onNavigateToCatAlphabetic,
            state = state,
            onEvent = savePointViewModel::onEvent
        )
    }
}
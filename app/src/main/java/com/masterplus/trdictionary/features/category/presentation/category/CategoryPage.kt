package com.masterplus.trdictionary.features.category.presentation.category

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.ListDetailContentType
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum

@ExperimentalMaterial3Api
@Composable
fun CategoryPage(
    onNavigateToSetting: ()->Unit,
    onNavigateToAlphabeticCat: (CategoryEnum)-> Unit,
    onNavigateToWordList: (CategoryEnum, SubCategoryEnum)->Unit,
    onNavigateToSearch: (CategoryEnum)->Unit,
    onNavigateToSavePoints: (SavePointNavigationArgs)->Unit,
    state: CategoryState,
    onEvent: (CategoryEvent) -> Unit,
    contentType: ListDetailContentType,
    displayFeatures: List<DisplayFeature>
){

    val listScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val detailListState = rememberLazyListState()

    LaunchedEffect(contentType){
        if(contentType == ListDetailContentType.SINGLE_PANE && !state.isDetailOpen){
            onEvent(CategoryEvent.CloseDetail)
        }
    }

    if(contentType == ListDetailContentType.DUAL_PANE){
        TwoPane(
            first = {
                 CategoryList(
                     state = state,
                     scrollBehavior = listScrollBehavior,
                     onNavigateToSetting = onNavigateToSetting,
                     onNavigateToDetail = { onEvent(CategoryEvent.OpenDetail(it)) }
                 )
            },
            second = {
                CategoryDetail(
                    category = state.selectedCategory,
                    onNavigateToAlphabeticCat = onNavigateToAlphabeticCat,
                    onNavigateToWordList = onNavigateToWordList,
                    onNavigateToSearch = onNavigateToSearch,
                    onNavigateToSavePoints = onNavigateToSavePoints,
                    lazyListState = detailListState,
                    isFullScreen = false,
                    onNavigateToBack = { onEvent(CategoryEvent.CloseDetail) }
                )
            },
            strategy = HorizontalTwoPaneStrategy(0.5f,12.dp),
            displayFeatures = displayFeatures
        )
    }else{
        SinglePane(
            state = state,
            onEvent = onEvent,
            listScrollBehavior = listScrollBehavior,
            onNavigateToAlphabeticCat = onNavigateToAlphabeticCat,
            onNavigateToWordList = onNavigateToWordList,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToSavePoints = onNavigateToSavePoints,
            onNavigateToSetting = onNavigateToSetting,
            detailListState = detailListState
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SinglePane(
    onNavigateToAlphabeticCat: (CategoryEnum)-> Unit,
    onNavigateToWordList: (CategoryEnum, SubCategoryEnum)->Unit,
    onNavigateToSearch: (CategoryEnum)->Unit,
    onNavigateToSavePoints: (SavePointNavigationArgs)->Unit,
    onNavigateToSetting: ()->Unit,
    state: CategoryState,
    onEvent: (CategoryEvent) -> Unit,
    listScrollBehavior: TopAppBarScrollBehavior,
    detailListState: LazyListState
) {
    val selectedCategory = state.selectedCategory
    if(state.isDetailOpen && selectedCategory != null){

        BackHandler {
            onEvent(CategoryEvent.CloseDetail)
        }

        CategoryDetail(
            category = selectedCategory,
            onNavigateToAlphabeticCat = onNavigateToAlphabeticCat,
            onNavigateToWordList = onNavigateToWordList,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToSavePoints = onNavigateToSavePoints,
            lazyListState = detailListState,
            isFullScreen = true,
            onNavigateToBack = { onEvent(CategoryEvent.CloseDetail) }
        )
    }else{
        CategoryList(
            state = state,
            scrollBehavior = listScrollBehavior,
            onNavigateToSetting = onNavigateToSetting,
            onNavigateToDetail = { onEvent(CategoryEvent.OpenDetail(it)) }
        )
    }


}
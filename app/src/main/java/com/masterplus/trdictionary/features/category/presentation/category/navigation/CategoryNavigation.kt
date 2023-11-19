package com.masterplus.trdictionary.features.category.presentation.category.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.window.layout.DisplayFeature
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.ListDetailContentType
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.features.category.presentation.category.CategoryPage
import com.masterplus.trdictionary.features.category.presentation.category.CategoryViewModel
import com.masterplus.trdictionary.features.category.presentation.category.SavePointNavigationArgs

const val RouteCategory = "category"

@ExperimentalMaterial3Api
fun NavGraphBuilder.categoryPage(
    onNavigateToSetting: () -> Unit,
    onNavigateToAlphabeticCat: (CategoryEnum) -> Unit,
    onNavigateToWordList: (CategoryEnum, SubCategoryEnum) -> Unit,
    onNavigateToSearch: (CategoryEnum) -> Unit,
    onNavigateToSavePoints: (SavePointNavigationArgs) -> Unit,
    contentType: ListDetailContentType,
    displayFeatures: List<DisplayFeature>
){
    composable(RouteCategory){
        val viewModel = hiltViewModel<CategoryViewModel>()

        val state by viewModel.state.collectAsStateWithLifecycle()

        CategoryPage(
            onNavigateToSetting = onNavigateToSetting,
            onNavigateToAlphabeticCat = onNavigateToAlphabeticCat,
            onNavigateToWordList = onNavigateToWordList,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToSavePoints = onNavigateToSavePoints,
            contentType = contentType,
            displayFeatures = displayFeatures,
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}

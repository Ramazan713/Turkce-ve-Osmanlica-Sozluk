package com.masterplus.trdictionary.features.category.presentation.category.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.features.category.presentation.category.CategoryPage

const val RouteCategory = "category"

@ExperimentalMaterial3Api
fun NavGraphBuilder.categoryPage(
    onNavigateToSetting: ()->Unit,
    onNavigateToSubCategory: (CategoryEnum)->Unit,
){
    composable(RouteCategory){
        CategoryPage(
            onNavigateToSubCategory = onNavigateToSubCategory,
            onNavigateToSetting = onNavigateToSetting
        )
    }
}

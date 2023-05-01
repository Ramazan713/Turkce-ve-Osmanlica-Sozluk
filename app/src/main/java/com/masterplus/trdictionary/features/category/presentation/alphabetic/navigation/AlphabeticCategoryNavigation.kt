package com.masterplus.trdictionary.features.category.presentation.alphabetic.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.features.category.presentation.alphabetic.AlphabeticCategoryPage

private const val routeName = "alphabeticCategory/{catId}"


fun NavController.navigateToAlphabeticCategory(catId: Int){
    this.navigate("alphabeticCategory/$catId")
}

@ExperimentalMaterial3Api
fun NavGraphBuilder.alphabeticCategoryPage(
    onNavigateBack: ()->Unit,
    onNavigateToWordList: (CategoryEnum, String)->Unit
){
    composable(
        routeName,
        arguments = listOf(
            navArgument("catId"){type = NavType.IntType},
        )
    ){stackEntry->
        val catId = stackEntry.arguments?.getInt("catId") ?: 0
        AlphabeticCategoryPage(
            catId = catId,
            onNavigateBack = onNavigateBack,
            onNavigateToWordList = onNavigateToWordList
        )
    }
}
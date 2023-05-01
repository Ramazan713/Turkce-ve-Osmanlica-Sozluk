package com.masterplus.trdictionary.features.category.presentation.sub_category.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.features.category.presentation.sub_category.SubCategoryPage

private const val routeName = "subCategory/{catId}"

fun NavController.navigateToSubCategory(catId: Int){
    this.navigate("subCategory/$catId")
}

@ExperimentalMaterial3Api
fun NavGraphBuilder.subCategoryPage(
    onNavigateToAlphabeticCat: (CategoryEnum)-> Unit,
    onNavigateToWordList: (CategoryEnum, SubCategoryEnum)->Unit,
    onNavigateToSearch: (CategoryEnum)->Unit,
    onNavigateToSavePoints: (String,List<Int>,Int)->Unit,
    onNavigateBack: ()->Unit
){
    composable(
        routeName,
        arguments = listOf(
            navArgument("catId"){type = NavType.IntType}
        )
    ){stackEntry->
        val catId = stackEntry.arguments?.getInt("catId") ?: 0
        SubCategoryPage(
            catId = catId,
            onNavigateBack = onNavigateBack,
            onNavigateToAlphabeticCat = onNavigateToAlphabeticCat,
            onNavigateToWordList = onNavigateToWordList,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToSavePoints = onNavigateToSavePoints
        )
    }
}
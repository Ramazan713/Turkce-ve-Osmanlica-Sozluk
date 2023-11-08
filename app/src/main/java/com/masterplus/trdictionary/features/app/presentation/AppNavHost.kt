package com.masterplus.trdictionary.features.app.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.DevicePosture
import com.masterplus.trdictionary.core.domain.enums.ListDetailContentType
import com.masterplus.trdictionary.features.app.domain.model.AppNavRoute
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.core.shared_features.premium.PremiumViewModel
import com.masterplus.trdictionary.features.category.presentation.alphabetic.navigation.alphabeticCategoryPage
import com.masterplus.trdictionary.features.category.presentation.alphabetic.navigation.navigateToAlphabeticCategory
import com.masterplus.trdictionary.features.category.presentation.category.navigation.categoryPage
import com.masterplus.trdictionary.features.home.presentation.navigation.homePage
import com.masterplus.trdictionary.features.list.presentation.archive_list.navigation.archiveListPage
import com.masterplus.trdictionary.features.list.presentation.archive_list.navigation.navigateToArchiveList
import com.masterplus.trdictionary.features.list.presentation.show_list.navigation.showListPage
import com.masterplus.trdictionary.features.savepoint.presentation.navigation.navigateToSelectSavePoint
import com.masterplus.trdictionary.features.savepoint.presentation.navigation.selectSavePoint
import com.masterplus.trdictionary.features.search.presentation.navigation.navigateToSearch
import com.masterplus.trdictionary.features.search.presentation.navigation.searchPage
import com.masterplus.trdictionary.features.settings.presentation.navigation.navigateToSettings
import com.masterplus.trdictionary.features.settings.presentation.navigation.settingsPage
import com.masterplus.trdictionary.features.word_detail.single_word_detail.navigation.navigateToSingleWordDetail
import com.masterplus.trdictionary.features.word_detail.single_word_detail.navigation.singleWordDetail
import com.masterplus.trdictionary.features.word_detail.word_category.navigation.navigateToListDetailCategoryWords
import com.masterplus.trdictionary.features.word_detail.word_category.navigation.listDetailCategoryWords
import com.masterplus.trdictionary.features.word_detail.word_list_for_list_detail.navigation.navigateToWordListForListDetail
import com.masterplus.trdictionary.features.word_detail.word_list_for_list_detail.navigation.wordListForListDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    premiumViewModel: PremiumViewModel,
    navController: NavHostController = rememberNavController(),
    windowWidthSizeClass: WindowWidthSizeClass,
    devicePosture: DevicePosture,
    displayFeatures: List<DisplayFeature>,
){

    val listDetailContentType = ListDetailContentType.from(windowWidthSizeClass,devicePosture)


    NavHost(navController, startDestination = AppNavRoute.Home.route, modifier) {

        homePage(
            premiumViewModel = premiumViewModel,
            onNavigateToWordDetail = {wordId->
                navController.navigateToSingleWordDetail(wordId)
            },
            onNavigateToSearch = {
                navController.navigateToSearch(CategoryEnum.AllDict.catId)
            },
            onNavigateToSetting = {navController.navigateToSettings()}
        )

        categoryPage(
            onNavigateToSetting = {navController.navigateToSettings()},
            contentType = listDetailContentType,
            displayFeatures = displayFeatures,
            onNavigateToAlphabeticCat = {catEnum->
                navController.navigateToAlphabeticCategory(catEnum.catId)
            },
            onNavigateToWordList = {categoryEnum, subCategoryEnum ->
                navController.navigateToListDetailCategoryWords(categoryEnum.catId,subCategoryEnum.subCatId)
            },
            onNavigateToSearch = {
                navController.navigateToSearch(it.catId)
            },
            onNavigateToSavePoints = {args->
                navController.navigateToSelectSavePoint(args.savePointTypeId,args.destinationIds,args.title)
            }
        )

        showListPage(
            onNavigateToDetailList = {listId ->
                navController.navigateToWordListForListDetail(listId)
            },
            onNavigateToSelectSavePoint = {title,filter,typeId ->
                navController.navigateToSelectSavePoint(typeId,filter,title)
            },
            onNavigateToArchive = {navController.navigateToArchiveList()},
            onNavigateToSettings = { navController.navigateToSettings() },
            windowWidthSizeClass = windowWidthSizeClass
        )

        archiveListPage(
            onNavigateBack = {navController.popBackStack()},
            onNavigateToDetailList = {listId->
                navController.navigateToWordListForListDetail(listId)
            },
            windowWidthSizeClass = windowWidthSizeClass
        )

        singleWordDetail(
            onNavigateBack = { navController.popBackStack() },
            onRelatedWordClicked = {wordId->
                navController.navigateToSingleWordDetail(wordId)
            },
            windowWidthSizeClass = windowWidthSizeClass
        )

        alphabeticCategoryPage(
            onNavigateBack = {navController.popBackStack()},
            onNavigateToWordList = {categoryEnum, c ->
                navController.navigateToListDetailCategoryWords(categoryEnum.catId,SubCategoryEnum.Alphabetic.subCatId,c)
            }
        )

        listDetailCategoryWords(
            onNavigateBack = { navController.popBackStack() },
            onRelatedWordClicked = { wordId ->
                navController.navigateToSingleWordDetail(wordId)
            },
            windowWidthSizeClass = windowWidthSizeClass,
            listDetailContentType = listDetailContentType,
            displayFeatures = displayFeatures
        )

        wordListForListDetail(
            onNavigateBack = { navController.popBackStack() },
            onRelatedWordClicked = {wordId->
                navController.navigateToSingleWordDetail(wordId)
            },
            windowWidthSizeClass = windowWidthSizeClass,
            listDetailContentType = listDetailContentType,
            displayFeatures = displayFeatures
        )

        searchPage(
            onBackPressed = {
                navController.popBackStack()
            },
            onRelatedWordClicked = {wordId->
                navController.navigateToSingleWordDetail(wordId)
            },
            windowWidthSizeClass = windowWidthSizeClass,
            listDetailContentType = listDetailContentType,
            displayFeatures = displayFeatures
        )

        selectSavePoint(
            onNavigateBack = {navController.popBackStack()},
            onNavigateToList = {listId, pos ->
                navController.navigateToWordListForListDetail(listId, pos)
            },
            onNavigateToCatAll = {catEnum,pos->
                navController.navigateToListDetailCategoryWords(catEnum.catId,SubCategoryEnum.All.subCatId,K.defaultCategoryAlphaChar,pos)
            },
            onNavigateToCatRandom = {catEnum,pos->
                navController.navigateToListDetailCategoryWords(catEnum.catId,SubCategoryEnum.Random.subCatId,K.defaultCategoryAlphaChar,pos)
            },
            onNavigateToCatAlphabetic = {catEnum,c,pos->
                navController.navigateToListDetailCategoryWords(catEnum.catId,SubCategoryEnum.Alphabetic.subCatId,c,pos)
            }
        )

        settingsPage(
            premiumViewModel = premiumViewModel,
            onNavigateBack = {navController.popBackStack()},
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
}
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
import com.masterplus.trdictionary.core.presentation.features.premium.PremiumViewModel
import com.masterplus.trdictionary.features.category.presentation.alphabetic.navigation.alphabeticCategoryPage
import com.masterplus.trdictionary.features.category.presentation.alphabetic.navigation.navigateToAlphabeticCategory
import com.masterplus.trdictionary.features.category.presentation.category.navigation.categoryPage
import com.masterplus.trdictionary.features.category.presentation.sub_category.navigation.navigateToSubCategory
import com.masterplus.trdictionary.features.category.presentation.sub_category.navigation.subCategoryPage
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
import com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail.navigation.navigateToSingleWordDetail
import com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail.navigation.singleWordDetail
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_category.navigation.navigateToWordListCategory
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_category.navigation.wordListCategory
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_for_list.navigation.navigateToWordListForList
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_for_list.navigation.wordListForList
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_category.navigation.navigateToWordsDetailCategory
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_category.navigation.wordsDetailCategory
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_list.navigation.navigateToWordsDetailList
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_list.navigation.wordsDetailList
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
            onNavigateToSubCategory = {categoryEnum->
                navController.navigateToSubCategory(categoryEnum.catId)
            },
            onNavigateToSetting = {navController.navigateToSettings()}
        )

        showListPage(
            onNavigateToDetailList = {listId ->
                navController.navigateToWordListForList(listId)
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
                navController.navigateToWordListForList(listId)
            },
            windowWidthSizeClass = windowWidthSizeClass
        )

        singleWordDetail(
            onNavigateBack = { navController.popBackStack() },
            onRelatedWordClicked = {wordId->
                navController.navigateToSingleWordDetail(wordId)
            }
        )

        subCategoryPage(
            onNavigateBack = {navController.popBackStack()},
            onNavigateToAlphabeticCat = {catEnum->
                navController.navigateToAlphabeticCategory(catEnum.catId)
            },
            onNavigateToWordList = {categoryEnum, subCategoryEnum ->
                navController.navigateToWordListCategory(categoryEnum.catId,subCategoryEnum.subCatId)
            },
            onNavigateToSearch = {
                navController.navigateToSearch(it.catId)
            },
            onNavigateToSavePoints = {title,destinationIds,typeId->
                navController.navigateToSelectSavePoint(typeId,destinationIds,title)
            }
        )

        alphabeticCategoryPage(
            onNavigateBack = {navController.popBackStack()},
            onNavigateToWordList = {categoryEnum, c ->
                navController.navigateToWordListCategory(categoryEnum.catId,SubCategoryEnum.Alphabetic.subCatId,c)
            }
        )

        wordListCategory(
            onNavigateBack = {navController.popBackStack()},
            onNavigateToWordsCategoryDetails = {catId,subCatId,c,newPos->
                navController.navigateToWordsDetailCategory(catId, subCatId,c,newPos)
            }
        )

        wordListForList(
            onNavigateToWordsListDetails = {listId,newPos->
                navController.navigateToWordsDetailList(listId,newPos)
            },
            onNavigateBack = {navController.popBackStack()}
        )

        wordsDetailCategory(
            onNavigateBack = {lastPos->
                navController.previousBackStackEntry?.savedStateHandle?.set("wordsDetailLastPos", lastPos)
                navController.popBackStack()
            },
            onRelatedWordClicked = {wordId->
                navController.navigateToSingleWordDetail(wordId)
            }
        )


        wordsDetailList(
            onNavigateBack = {lastPos->
                navController.previousBackStackEntry?.savedStateHandle?.set("wordsDetailLastPos", lastPos)
                navController.popBackStack()
            },
            onRelatedWordClicked = {wordId->
                navController.navigateToSingleWordDetail(wordId)
            }
        )

        searchPage(
            onBackPressed = {
                navController.popBackStack()
            },
            onNavigateToWordDetail = {wordId,removeStack->
                val navOptions = if (removeStack)NavOptions.Builder()
                    .setPopUpTo(navController.currentDestination?.route?:"",inclusive = true)
                    .build() else null
                navController.navigateToSingleWordDetail(wordId,navOptions)
            }
        )

        selectSavePoint(
            onNavigateBack = {navController.popBackStack()},
            onNavigateToList = {listId, pos ->
                navController.navigateToWordListForList(listId, pos)
            },
            onNavigateToCatAll = {catEnum,pos->
                navController.navigateToWordListCategory(catEnum.catId,SubCategoryEnum.All.subCatId,K.defaultCategoryAlphaChar,pos)
            },
            onNavigateToCatRandom = {catEnum,pos->
                navController.navigateToWordListCategory(catEnum.catId,SubCategoryEnum.Random.subCatId,K.defaultCategoryAlphaChar,pos)
            },
            onNavigateToCatAlphabetic = {catEnum,c,pos->
                navController.navigateToWordListCategory(catEnum.catId,SubCategoryEnum.Alphabetic.subCatId,c,pos)
            }
        )

        settingsPage(
            premiumViewModel = premiumViewModel,
            onNavigateBack = {navController.popBackStack()}
        )
    }
}
package com.masterplus.trdictionary.features.word_detail.presentation.word_detail_list.word_category.navigation

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.*
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.layout.DisplayFeature
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.ListDetailContentType
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailViewModel
import com.masterplus.trdictionary.features.word_detail.presentation.word_detail_list.word_category.WordCategoryPage
import com.masterplus.trdictionary.features.word_detail.presentation.word_detail_list.word_category.WordCategoryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

const val RouteWordsDetailCategory = "wordsDetailCategory/{catId}/{subCatId}/{c}/{pos}"


data class WordCategoryArgs(
    val catId: Int,
    val subCatId: Int,
    val c: String?,
    val pos: Int,
){
    constructor(savedStateHandle: SavedStateHandle): this(
        checkNotNull(savedStateHandle.get<String>("catId")).toIntOrNull() ?: 1,
        checkNotNull(savedStateHandle.get<String>("subCatId")) .toIntOrNull() ?: 1,
        savedStateHandle.get<String?>("c")?.let { if(it.trim() == K.defaultCategoryAlphaChar) null else it },
        checkNotNull(savedStateHandle.get<String>("pos")).toIntOrNull() ?: 1,
    )

    val cat get() = CategoryEnum.fromCatId(catId)
    val subCat get() = SubCategoryEnum.fromSubCatId(subCatId)
}


fun NavController.navigateToWordsDetailCategory(catId: Int, subCatId: Int, c: String? = K.defaultCategoryAlphaChar, pos: Int = 0){
    navigate("wordsDetailCategory/${catId}/${subCatId}/${c?:K.defaultCategoryAlphaChar}/${pos}")
}


@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
fun NavGraphBuilder.wordsDetailCategory(
    onNavigateBack: () -> Unit,
    onRelatedWordClicked: (Int) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    listDetailContentType: ListDetailContentType,
    displayFeatures: List<DisplayFeature>
){
    composable(
        RouteWordsDetailCategory,
        arguments = listOf(
            navArgument("catId"){type = NavType.StringType},
            navArgument("subCatId"){type = NavType.StringType},
            navArgument("c"){type = NavType.StringType},
            navArgument("pos"){type = NavType.StringType},
        ),
        deepLinks = listOf(
            navDeepLink {
                action = Intent.ACTION_VIEW
                uriPattern = "${K.DeepLink.categoryDetailBaseUrl}/{catId}/{subCatId}/{c}/{pos}"
            }
        )
    ){
        val wordCategoryViewModel = hiltViewModel<WordCategoryViewModel>()

        val wordListViewModel = hiltViewModel<WordsListDetailViewModel>()

        val words = wordCategoryViewModel.words.collectAsLazyPagingItems()


        WordCategoryPage(
            state = wordListViewModel.state,
            onEvent = wordListViewModel::onEvent,
            listDetailContentType = listDetailContentType,
            windowWidthSizeClass = windowWidthSizeClass,
            displayFeatures = displayFeatures,
            words = words,
            savePointInfo = wordCategoryViewModel.savePointInfo,
            onNavigateBack = onNavigateBack,
            initPos = wordCategoryViewModel.args.pos
        )
    }
}

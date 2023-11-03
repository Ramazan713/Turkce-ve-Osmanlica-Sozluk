package com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_category.navigation

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
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_category.WordListCategoryPage
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_category.WordListCategoryViewModel

const val RouteWordListCategory = "wordListCategory/{catId}/{subCatId}/{c}/{pos}"


data class WordListCategoryArgs(
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


fun NavController.navigateToWordListCategory(catId: Int,subCatId: Int,c: String = K.defaultCategoryAlphaChar,pos: Int = 0){
    navigate("wordListCategory/${catId}/${subCatId}/${c}/${pos}")
}


@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
fun NavGraphBuilder.wordListCategory(
    onNavigateToWordsCategoryDetails: (Int,Int,String?,Int)->Unit,
    onNavigateBack: ()->Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
){
    composable(
        RouteWordListCategory,
        arguments = listOf(
            navArgument("catId"){type = NavType.StringType},
            navArgument("subCatId"){type = NavType.StringType},
            navArgument("c"){type = NavType.StringType},
            navArgument("pos"){type = NavType.StringType},
        ),
        deepLinks = listOf(
            navDeepLink {
                action = Intent.ACTION_VIEW
                uriPattern = "${K.DeepLink.categoryBaseUrl}/{catId}/{subCatId}/{c}/{pos}"
            }
        )
    ){stackEntry->
        val lastPos = stackEntry.savedStateHandle.get<Int?>("wordsDetailLastPos")
        val wordListCategoryViewModel: WordListCategoryViewModel = hiltViewModel()
        val args = wordListCategoryViewModel.args

        val pagingWords = wordListCategoryViewModel.pagingWords.collectAsLazyPagingItems()

        WordListCategoryPage(
            pos = args.pos,
            wordListPos = lastPos,
            onNavigateBack = onNavigateBack,
            onNavigateToWordsCategoryDetails = {newPos->
                onNavigateToWordsCategoryDetails(args.catId,args.subCatId,args.c,newPos)
            },
            state = wordListCategoryViewModel.state,
            sharedState = wordListCategoryViewModel.sharedState,
            onSharedEvent = wordListCategoryViewModel::onSharedEvent,
            pagingWords = pagingWords,
            onItemScrolledNewPos = {
                stackEntry.savedStateHandle.remove<Int>("wordsDetailLastPos")
            },
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
}

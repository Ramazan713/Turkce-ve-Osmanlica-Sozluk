package com.masterplus.trdictionary.features.word_detail.word_category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.save_point_info.SavePointCategoryInfoUseCases
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.WordDetailsCompletedUseCases
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailState
import com.masterplus.trdictionary.features.word_detail.word_category.navigation.WordCategoryArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WordCategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    wordDetailUseCases: WordDetailsCompletedUseCases,
    savePointCategoryInfoUseCases: SavePointCategoryInfoUseCases,
): ViewModel() {

    val args = WordCategoryArgs(savedStateHandle)

    var state by mutableStateOf(WordsListDetailState())
        private set


    val savePointInfo = savePointCategoryInfoUseCases(
        args.catId,
        args.subCatId,
        args.c
    )

    val words = wordDetailUseCases.getCategoryCompletedWordsPaging(
        categoryEnum = args.cat,
        subCategoryEnum = args.subCat,
        c = args.c
    ).cachedIn(viewModelScope)
}
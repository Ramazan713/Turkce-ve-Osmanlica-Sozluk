package com.masterplus.trdictionary.features.word_detail.word_list_for_list_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.WordDetailsCompletedUseCases
import com.masterplus.trdictionary.features.word_detail.word_list_for_list_detail.navigation.WordListForListDetailArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordListForListDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    wordDetailsCompletedUseCases: WordDetailsCompletedUseCases,
    private val listRepo: ListRepo,
): ViewModel() {

    val args = WordListForListDetailArgs(savedStateHandle)

    val pagingWords = wordDetailsCompletedUseCases.getListCompletedWordsPaging(args.listId)
        .cachedIn(viewModelScope)

    val savePointDestination = SavePointDestination.List(args.listId)

    var listName by mutableStateOf<String>("")
        private set

    init {
        loadListName()
    }

    private fun loadListName(){
        viewModelScope.launch {
            listRepo.getListById(args.listId)?.name?.let { name->
                listName = name
            }
        }
    }

}
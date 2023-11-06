package com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_for_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.presentation.features.share.domain.use_cases.ShareWordUseCases
import com.masterplus.trdictionary.features.word_detail.domain.use_case.word_details_basic.WordDetailsSimpleUseCases
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared.WordListSharedEvent
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared.WordListSharedState
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared.WordListSharedUiEvent
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_for_list.navigation.WordListForListArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class WordListForListViewModel @Inject constructor(
    wordDetailsSimpleUseCases: WordDetailsSimpleUseCases,
    private val listRepo: ListRepo,
    private val wordShareUseCases: ShareWordUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    val args = WordListForListArgs(savedStateHandle)

    val pagingWords = wordDetailsSimpleUseCases.getListSimpleWords(args.listId).cachedIn(viewModelScope)

    var sharedState by mutableStateOf(WordListSharedState())
        private set

    var state by mutableStateOf(WordListState())
        private set

    init {
        loadData(args.listId)
    }

    fun onSharedEvent(event: WordListSharedEvent){
        when(event){
            is WordListSharedEvent.ShowDialog -> {
                sharedState = sharedState.copy(
                    showDialog = event.showDialog,
                    dialogEvent = event.dialogEvent
                )
            }
            is WordListSharedEvent.ShowModal -> {
                sharedState = sharedState.copy(
                    showModal = event.showModal,
                    modalEvent = event.modalEvent
                )
            }
            is WordListSharedEvent.EvaluateShareWord -> {
//                viewModelScope.launch {
//                    val shareResult = wordShareUseCases.shareWordList(event.savePointDestination,event.savePointType,
//                        event.pos,event.word,event.shareItemEnum,
//                        K.DeepLink.categoryBaseUrl)
//                    sharedState = sharedState.copy(uiEvent = WordListSharedUiEvent.ShareWord(shareResult))
//                }
            }
            is WordListSharedEvent.ClearUiEvent -> {

            }
        }
    }

    fun loadData(listId: Int){
        viewModelScope.launch {
            val listModel = listRepo.getListById(listId)
            val savePointDestination = SavePointDestination.List(listId)
            state = state.copy(
                list = listModel,
                savePointDestination = savePointDestination
            )
        }
    }
}
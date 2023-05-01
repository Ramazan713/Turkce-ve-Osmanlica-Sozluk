package com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.features.word_detail.domain.use_case.save_point_info.SavePointCategoryInfoUseCases
import com.masterplus.trdictionary.features.word_detail.domain.use_case.share.ShareWordUseCases
import com.masterplus.trdictionary.features.word_detail.domain.use_case.word_details_basic.WordDetailsSimpleUseCases
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared.WordListSharedEvent
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared.WordListSharedState
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared.WordListSharedUiEvent
import com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_category.navigation.WordListCategoryArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordListCategoryViewModel @Inject constructor(
    wordDetailUseCases: WordDetailsSimpleUseCases,
    private val savePointCategoryInfoUseCases: SavePointCategoryInfoUseCases,
    private val wordShareUseCases: ShareWordUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    val args = WordListCategoryArgs(savedStateHandle)

    val pagingWords = wordDetailUseCases.getCategorySimpleWords(
        args.cat,args.subCat,args.c
    ).cachedIn(viewModelScope)

    var sharedState by mutableStateOf(WordListSharedState())
        private set

    var state by mutableStateOf(WordListCategoryState())
        private set

    init {
        loadData()
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
                viewModelScope.launch {
                    val shareResult = wordShareUseCases.shareWordList(event.savePointDestination,event.savePointType,
                        event.pos,event.word,event.shareItemEnum, K.DeepLink.categoryBaseUrl)
                    sharedState = sharedState.copy(uiEvent = WordListSharedUiEvent.ShareWord(shareResult))
                }
            }
             is WordListSharedEvent.ClearUiEvent ->{
                 sharedState = sharedState.copy(uiEvent = null)
             }
         }
    }


    private fun loadData(){
        viewModelScope.launch {
            savePointCategoryInfoUseCases(args.catId,args.subCatId,args.c).let { result->
                state = state.copy(
                    catEnum = result.catEnum,
                    subCategoryEnum = result.subCategoryEnum,
                    savePointTitle = result.savePointTitle,
                    savePointDestination = result.savePointDestination
                )
            }
        }
    }
}
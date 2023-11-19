package com.masterplus.trdictionary.core.shared_features.select_list.list_menu_dia

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.domain.repo.ListWordsRepo
import com.masterplus.trdictionary.core.domain.use_cases.ListInFavoriteControlForDeletionUseCase
import com.masterplus.trdictionary.core.domain.use_cases.list_words.ListWordsUseCases
import com.masterplus.trdictionary.core.shared_features.select_list.constants.SelectListMenuEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectListMenuViewModel @Inject constructor(
    private val listWordsUseCases: ListWordsUseCases,
    private val listWordsRepo: ListWordsRepo,
    private val listInFavoriteUseCase: ListInFavoriteControlForDeletionUseCase
): ViewModel(){

    private val _state = MutableStateFlow(SelectListMenuState())
    val state: StateFlow<SelectListMenuState> = _state.asStateFlow()

    private var loadDataJob: Job? = null

    fun onEvent(event: SelectListMenuEvent){
        when(event){
            is SelectListMenuEvent.LoadData -> {
                loadData(event)
            }
            is SelectListMenuEvent.AddToFavorite -> {
                viewModelScope.launch {
                    addFavoriteWord(event.wordId)
                }
            }
            is SelectListMenuEvent.ShowDialog -> {
                _state.update { it.copy(showDialog = event.showDialog, dialogEvent = event.dialogEvent)}
            }
            is SelectListMenuEvent.AddOrAskFavorite -> {
                viewModelScope.launch {
                    listInFavoriteUseCase(_state.value.listIdControl,true).let { showDia->
                        if(showDia){
                            _state.update { state->
                                state.copy(
                                    showDialog = true,
                                    dialogEvent = SelectListMenuDialogEvent.AskFavoriteDelete(event.wordId)
                                )
                            }
                        }else{
                            addFavoriteWord(event.wordId)
                        }
                    }
                }
            }
        }
    }

    private suspend fun addFavoriteWord(wordId: Int){
        listWordsUseCases.addFavoriteListWords.invoke(wordId)
    }

    private fun getListMenuItems(inFavorite: Boolean, isInList: Boolean): List<SelectListMenuEnum>{
        return mutableListOf<SelectListMenuEnum>().apply {
            if(inFavorite) add(SelectListMenuEnum.AddedFavorite)
                else add(SelectListMenuEnum.AddFavorite)
            if(isInList) add(SelectListMenuEnum.AddedList)
                else add(SelectListMenuEnum.AddList)
        }.toList()
    }

    private fun loadData(event: SelectListMenuEvent.LoadData){
        _state.update { it.copy(listIdControl = event.listId)}
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            val hasInFavoriteListFlow = listWordsRepo.hasWordInFavoriteListFlow(event.wordId)
            val hasInRemovableListFlow = listWordsRepo.hasWordInRemovableListFlow(event.wordId)

            combine(hasInFavoriteListFlow,hasInRemovableListFlow){inFavorite,inLists->{
                CombineResult(
                    isFavorite = inFavorite,
                    isAddedToList = inLists,
                    listMenuItems = getListMenuItems(inFavorite,inLists),
                )
            }}.collectLatest {resultFunc->
                val result = resultFunc()
                _state.update { state->
                    state.copy(
                        isFavorite = result.isFavorite,
                        isAddedToList = result.isAddedToList,
                        listMenuItems = result.listMenuItems,
                    )
                }
            }
        }
    }
}

private data class CombineResult(
    val isFavorite: Boolean,
    val isAddedToList: Boolean,
    val listMenuItems: List<SelectListMenuEnum>
)

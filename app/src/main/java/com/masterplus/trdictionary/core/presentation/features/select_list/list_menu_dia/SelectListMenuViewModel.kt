package com.masterplus.trdictionary.core.presentation.features.select_list.list_menu_dia

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.domain.repo.ListWordsRepo
import com.masterplus.trdictionary.core.domain.use_cases.list_words.ListWordsUseCases
import com.masterplus.trdictionary.core.presentation.features.select_list.constants.SelectListMenuEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectListMenuViewModel @Inject constructor(
    private val listWordsUseCases: ListWordsUseCases,
    private val listWordsRepo: ListWordsRepo,
    private val listRepo: ListRepo
): ViewModel(){

    var state by mutableStateOf(SelectListMenuState())
        private set

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
                state = state.copy(showDialog = event.showDialog, dialogEvent = event.dialogEvent)
            }
            is SelectListMenuEvent.AddOrAskFavorite -> {
                viewModelScope.launch {
                    val listId = state.listIdControl ?: kotlin.run {
                        return@launch addFavoriteWord(event.wordId)
                    }
                    if(!listRepo.isFavoriteList(listId))
                        return@launch addFavoriteWord(event.wordId)
                    state = state.copy(
                        showDialog = true,
                        dialogEvent = SelectListMenuDialogEvent.AskFavoriteDelete(event.wordId)
                    )
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
        state = state.copy(listIdControl = event.listId)
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            val hasInFavoriteListFlow = listWordsRepo.hasWordInFavoriteListFlow(event.wordId)
            val hasInRemovableListFlow = listWordsRepo.hasWordInRemovableListFlow(event.wordId)

            combine(hasInFavoriteListFlow,hasInRemovableListFlow){inFavorite,inLists->{
                state.copy(
                    isFavorite = inFavorite,
                    isAddedToList = inLists,
                    listMenuItems = getListMenuItems(inFavorite,inLists),
              )
            }}.collectLatest {
                state = it()
            }

        }
    }

}
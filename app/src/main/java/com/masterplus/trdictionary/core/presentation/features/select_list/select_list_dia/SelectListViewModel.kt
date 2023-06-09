package com.masterplus.trdictionary.core.presentation.features.select_list.select_list_dia

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.domain.use_cases.list_words.ListWordsUseCases
import com.masterplus.trdictionary.core.domain.use_cases.lists.ListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectListViewModel @Inject constructor(
    private val listUseCases: ListUseCases,
    private val listWordsUseCases: ListWordsUseCases,
    private val appPreferences: AppPreferences
): ViewModel(){

    var state by mutableStateOf(SelectListState())
        private set

    private var loadDataJob: Job? = null


    fun onEvent(event: SelectListEvent){
        when(event){
            is SelectListEvent.AddToList -> {
                viewModelScope.launch {
                    listWordsUseCases.addListWords.invoke(event.listView,event.wordId)
                }
            }
            is SelectListEvent.LoadData -> {
                loadData(event)
            }
            is SelectListEvent.NewList -> {
                viewModelScope.launch {
                    listUseCases.insertList.invoke(event.listName)
                }
            }
            is SelectListEvent.AddOrAskToList -> {
                viewModelScope.launch {
                    val listViewId = event.selectableListView.listView.id ?: return@launch
                    if(!event.selectableListView.isSelected) return@launch addToList(event)
                    if(state.listIdControl != listViewId) return@launch addToList(event)
                    state = state.copy(
                        showDialog = true, dialogEvent = SelectListDialogEvent.AskListDelete(
                            event.wordId,
                            event.selectableListView.listView
                        )
                    )
                }
            }
            is SelectListEvent.ShowDialog -> {
                state = state.copy(showDialog = event.showDialog, dialogEvent = event.dialogEvent)
            }
        }
    }
    private suspend fun addToList(event: SelectListEvent.AddOrAskToList){
        listWordsUseCases.addListWords.invoke(event.selectableListView.listView,event.wordId)
    }

    private fun loadData(event: SelectListEvent.LoadData){
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            val useArchiveAsList = appPreferences.getItem(KPref.useArchiveLikeList)
            listWordsUseCases.getSelectableLists(useArchiveAsList,event.wordId).collectLatest { lists->
                state = state.copy(items = lists, listIdControl = event.listIdControl)
            }
        }
    }
}
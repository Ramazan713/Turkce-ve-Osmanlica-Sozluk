package com.masterplus.trdictionary.core.shared_features.select_list.select_list_dia

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferencesApp
import com.masterplus.trdictionary.core.domain.use_cases.list_words.ListWordsUseCases
import com.masterplus.trdictionary.core.domain.use_cases.lists.ListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectListViewModel @Inject constructor(
    private val listUseCases: ListUseCases,
    private val listWordsUseCases: ListWordsUseCases,
    private val settingsPreferences: SettingsPreferencesApp
): ViewModel(){

    private val _state = MutableStateFlow(SelectListState())
    val state: StateFlow<SelectListState> = _state.asStateFlow()

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
                    if(_state.value.listIdControl != listViewId) return@launch addToList(event)
                    _state.update { state-> state.copy(
                        showDialog = true, dialogEvent = SelectListDialogEvent.AskListDelete(
                            event.wordId,
                            event.selectableListView.listView
                        )
                    )}
                }
            }
            is SelectListEvent.ShowDialog -> {
                _state.update { state-> state.copy(
                    showDialog = event.showDialog, dialogEvent = event.dialogEvent
                )}
            }
        }
    }
    private suspend fun addToList(event: SelectListEvent.AddOrAskToList){
        listWordsUseCases.addListWords.invoke(event.selectableListView.listView,event.wordId)
    }

    private fun loadData(event: SelectListEvent.LoadData){
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            val prefData = settingsPreferences.getData()
            listWordsUseCases.getSelectableLists(prefData.useArchiveLikeList,event.wordId).collectLatest { lists->
                _state.update { state-> state.copy(
                    items = lists, listIdControl = event.listIdControl
                )}
            }
        }
    }
}
package com.masterplus.trdictionary.features.list.presentation.show_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.use_cases.lists.ListUseCases
import com.masterplus.trdictionary.core.domain.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowListViewModel @Inject constructor(
    private val listUseCases: ListUseCases
): ViewModel(){

    var state by mutableStateOf(ShowListState())
        private set

    private var loadDataJob: Job? = null

    init {
        loadData()
    }

    fun onEvent(event: ShowListEvent){
        when(event){
            is ShowListEvent.ShowDialog -> {
                state = state.copy(
                    showDialog = event.showDialog,
                    dialogEvent = event.dialogEvent
                )
            }
            is ShowListEvent.ShowModal -> {
                state = state.copy(
                    showModal = event.showModal,
                    modalEvent = event.modalEvent
                )
            }
            is ShowListEvent.AddNewList -> {
                viewModelScope.launch {
                    listUseCases.insertList.invoke(event.listName)
                    state = state.copy(message = UiText.Resource(R.string.successfully_added))
                }
            }
            is ShowListEvent.Archive -> {
                viewModelScope.launch {
                    listUseCases.updateList.invoke(event.listView, newIsArchive = true)
                    state = state.copy(message = UiText.Resource(R.string.successfully_archived))
                }
            }
            is ShowListEvent.Copy -> {
                viewModelScope.launch {
                    listUseCases.copyList.invoke(event.listView)
                    state = state.copy(message = UiText.Resource(R.string.successfully_copied))
                }
            }
            is ShowListEvent.Delete -> {
                viewModelScope.launch {
                    listUseCases.deleteList.invoke(event.listView)
                    state = state.copy(message = UiText.Resource(R.string.successfully_deleted))
                }
            }
            is ShowListEvent.Rename -> {
                viewModelScope.launch {
                    listUseCases.updateList.invoke(event.listView, newName = event.newName)
                    state = state.copy(message = UiText.Resource(R.string.success))
                }
            }
            is ShowListEvent.ClearMessage -> {
               state = state.copy(message = null)
            }
        }
    }


    private fun loadData(){
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            listUseCases.getLists.invoke(false).collectLatest {items->
                state = state.copy(
                    items = items
                )
            }
        }
    }



}
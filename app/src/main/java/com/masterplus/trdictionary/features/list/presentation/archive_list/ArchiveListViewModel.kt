package com.masterplus.trdictionary.features.list.presentation.archive_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.core.domain.use_cases.lists.ListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveListViewModel @Inject constructor(
    private val listUseCases: ListUseCases
): ViewModel(){

    var state by mutableStateOf(ArchiveListState())
        private set

    private var loadDataJob: Job? = null

    init {
        loadData()
    }

    fun onEvent(event: ArchiveListEvent){
        when(event){
            is ArchiveListEvent.ShowDialog -> {
                state = state.copy(
                    showDialog = event.showDialog,
                    dialogEvent = event.dialogEvent
                )
            }
            is ArchiveListEvent.UnArchive -> {
                viewModelScope.launch {
                    listUseCases.updateList.invoke(event.listView, newIsArchive = false)
                    state = state.copy(message = UiText.Resource(R.string.successfully_unarchive))
                }
            }

            is ArchiveListEvent.Delete -> {
                viewModelScope.launch {
                    listUseCases.deleteList.invoke(event.listView)
                    state = state.copy(message = UiText.Resource(R.string.successfully_deleted))
                }
            }
            is ArchiveListEvent.Rename -> {
                viewModelScope.launch {
                    listUseCases.updateList.invoke(event.listView, newName = event.newName)
                    state = state.copy(message = UiText.Resource(R.string.success))
                }
            }
            is ArchiveListEvent.ClearMessage -> {
               state = state.copy(message = null)
            }
        }
    }


    private fun loadData(){
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            listUseCases.getLists.invoke(true).collectLatest {items->
                state = state.copy(
                    items = items
                )
            }
        }
    }

}
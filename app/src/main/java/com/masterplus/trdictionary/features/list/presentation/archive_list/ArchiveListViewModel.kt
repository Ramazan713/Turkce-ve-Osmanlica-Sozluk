package com.masterplus.trdictionary.features.list.presentation.archive_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.UiText
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
class ArchiveListViewModel @Inject constructor(
    private val listUseCases: ListUseCases
): ViewModel(){

    private val _state = MutableStateFlow(ArchiveListState())
    val state: StateFlow<ArchiveListState> = _state.asStateFlow()

    private var loadDataJob: Job? = null

    init {
        loadData()
    }

    fun onEvent(event: ArchiveListEvent){
        when(event){
            is ArchiveListEvent.ShowDialog -> {
                _state.update { it.copy(
                    showDialog = event.showDialog,
                    dialogEvent = event.dialogEvent
                )}
            }
            is ArchiveListEvent.UnArchive -> {
                viewModelScope.launch {
                    listUseCases.updateList.invoke(event.listView, newIsArchive = false)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.successfully_unarchive)
                    )}
                }
            }

            is ArchiveListEvent.Delete -> {
                viewModelScope.launch {
                    listUseCases.deleteList.invoke(event.listView)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.successfully_deleted)
                    )}
                }
            }
            is ArchiveListEvent.Rename -> {
                viewModelScope.launch {
                    listUseCases.updateList.invoke(event.listView, newName = event.newName)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.success)
                    )}
                }
            }
            is ArchiveListEvent.ClearMessage -> {
                _state.update { it.copy(message = null)}
            }
        }
    }


    private fun loadData(){
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            listUseCases.getLists.invoke(true).collectLatest {items->
                _state.update { it.copy(items = items)}
            }
        }
    }

}
package com.masterplus.trdictionary.core.shared_features.edit_savepoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.domain.use_cases.savepoint.SavePointsUseCases
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
class EditSavePointViewModel @Inject constructor(
    private val savePointsUseCases: SavePointsUseCases
): ViewModel(){


    private val _state = MutableStateFlow(EditSavePointState())
    val state: StateFlow<EditSavePointState> = _state.asStateFlow()


    private var loadDataJob: Job? = null

    fun onEvent(event: EditSavePointEvent){
        when(event){
            is EditSavePointEvent.Delete -> {
                viewModelScope.launch {
                    savePointsUseCases.deleteSavePoint(event.savePoint)
                    _state.update { state->
                        val updatedSelectedSavePoint = if(event.savePoint == state.selectedSavePoint) null else state.selectedSavePoint
                        state.copy(
                            message = UiText.Resource(R.string.successfully_deleted),
                            selectedSavePoint = updatedSelectedSavePoint
                        )
                    }
                }
            }
            is EditSavePointEvent.EditTitle -> {
                viewModelScope.launch{
                    savePointsUseCases.updateSavePoint(event.savePoint.copy(title = event.title))
                    _state.update { it.copy(message = UiText.Resource(R.string.successfully_updated))}
                }
            }
            is EditSavePointEvent.LoadData -> {
                loadData(event)
            }
            is EditSavePointEvent.Select -> {
                _state.update { it.copy(selectedSavePoint = event.savePoint)}
            }
            is EditSavePointEvent.AddSavePoint -> {
                viewModelScope.launch {
                    SavePointDestination.fromDestinationId(event.destinationId,event.saveKey)?.let { destination->
                        savePointsUseCases.insertSavePoint(
                            itemPosIndex = event.pos,
                            destination = destination,
                            title = event.title
                        )
                        _state.update { it.copy(message = UiText.Resource(R.string.successfully_added))}
                    }
                }
            }
            is EditSavePointEvent.OverrideSavePoint -> {
                viewModelScope.launch {
                    _state.value.selectedSavePoint?.let { savePoint ->
                        savePointsUseCases.updateSavePoint(
                            savePoint.copy(itemPosIndex = event.pos)
                        )
                        _state.update{ it.copy(message = UiText.Resource(R.string.successfully_updated)) }
                    }
                }
            }
            is EditSavePointEvent.ShowDialog -> {
                _state.update { state-> state.copy(
                    dialogEvent = event.dialogEvent, showDialog = event.showDialog
                )}
            }
            is EditSavePointEvent.ClearMessage -> {
                _state.update { it.copy(message = null)}
            }
        }
    }


    private fun loadData(event: EditSavePointEvent.LoadData){
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            savePointsUseCases.getSavePoints(event.saveKey).collectLatest { items->
                _state.update { it.copy(savePoints = items)}
            }
        }
    }

}
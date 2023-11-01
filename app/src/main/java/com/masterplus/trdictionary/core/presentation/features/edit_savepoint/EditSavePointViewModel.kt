package com.masterplus.trdictionary.core.presentation.features.edit_savepoint

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.core.domain.use_cases.savepoint.SavePointsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditSavePointViewModel @Inject constructor(
    private val savePointsUseCases: SavePointsUseCases
): ViewModel(){

    var state by mutableStateOf(EditSavePointState())
        private set

    private var loadDataJob: Job? = null

    fun onEvent(event: EditSavePointEvent){
        when(event){
            is EditSavePointEvent.Delete -> {
                viewModelScope.launch {
                    savePointsUseCases.deleteSavePoint(event.savePoint)
                    state = state.copy(message = UiText.Resource(R.string.successfully_deleted))
                }
            }
            is EditSavePointEvent.EditTitle -> {
                viewModelScope.launch{
                    savePointsUseCases.updateSavePoint(event.savePoint.copy(title = event.title))
                    state = state.copy(message = UiText.Resource(R.string.successfully_updated))
                }
            }
            is EditSavePointEvent.LoadData -> {
                loadData(event)
            }
            is EditSavePointEvent.Select -> {
                state = state.copy(selectedSavePoint = event.savePoint)
            }
            is EditSavePointEvent.AddSavePoint -> {
                viewModelScope.launch {
                    SavePointDestination.fromDestinationId(event.destinationId,event.saveKey)?.let { destination->
                        savePointsUseCases.insertSavePoint(
                            itemPosIndex = event.pos,
                            destination = destination,
                            title = event.title
                        )
                        state = state.copy(message = UiText.Resource(R.string.successfully_added))
                    }
                }
            }
            is EditSavePointEvent.OverrideSavePoint -> {
                viewModelScope.launch {
                    state.selectedSavePoint?.let { savePoint ->
                        savePointsUseCases.updateSavePoint(
                            savePoint.copy(itemPosIndex = event.pos)
                        )
                        state = state.copy(message = UiText.Resource(R.string.successfully_updated))
                    }
                }
            }
            is EditSavePointEvent.ShowDialog -> {
                state = state.copy(dialogEvent = event.dialogEvent, showDialog = event.showDialog)
            }
            is EditSavePointEvent.ClearMessage -> {
               state = state.copy(message = null)
            }
        }
    }


    private fun loadData(event: EditSavePointEvent.LoadData){
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            savePointsUseCases.getSavePoints(event.saveKey).collectLatest { items->
                state = state.copy(savePoints = items)
            }
        }
    }

}
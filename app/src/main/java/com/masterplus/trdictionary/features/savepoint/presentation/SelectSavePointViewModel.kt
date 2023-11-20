package com.masterplus.trdictionary.features.savepoint.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.use_cases.savepoint.SavePointsUseCases
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.features.savepoint.presentation.constants.SelectSavePointMenuItem
import com.masterplus.trdictionary.features.savepoint.presentation.navigation.SelectSavePointArgs
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
class SelectSavePointViewModel @Inject constructor(
    private val savePointsUseCases: SavePointsUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val args = SelectSavePointArgs(savedStateHandle)

    private val _state = MutableStateFlow(SelectSavePointState())
    val state: StateFlow<SelectSavePointState> = _state.asStateFlow()

    private val filterFlow = MutableStateFlow(SelectSavePointMenuItem.All)

    private var loadDataJob: Job? = null

    init {
        loadData()
    }

    fun onEvent(event: SelectSavePointEvent){
        when(event){
            is SelectSavePointEvent.Delete -> {
                viewModelScope.launch {
                    savePointsUseCases.deleteSavePoint(event.savePoint)
                    _state.update { state->
                        val updatedSelectedSavePoint = if(event.savePoint == state.currentSelectedSavePoint) null else
                            state.currentSelectedSavePoint
                        state.copy(
                            message = UiText.Resource(R.string.successfully_deleted),
                            selectedSavePoint = updatedSelectedSavePoint
                        )
                    }

                }
            }
            is SelectSavePointEvent.EditTitle -> {
                viewModelScope.launch {
                    savePointsUseCases.updateSavePoint(event.savePoint.copy(title = event.title))
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.successfully_updated)
                    )}
                }
            }
            is SelectSavePointEvent.LoadSavePoint -> {
                loadSavePoint()
            }
            is SelectSavePointEvent.Select -> {
                _state.update { it.copy(selectedSavePoint = event.savePoint)}
            }
            is SelectSavePointEvent.ShowDialog -> {
                _state.update { it.copy(
                    showDialog = event.showDialog,
                    modalDialog = event.dialogEvent
                )}
            }
            is SelectSavePointEvent.SelectDropdownMenuItem -> {
                viewModelScope.launch {
                    _state.update { it.copy( selectedDropdownItem = event.item)}
                    filterFlow.emit(event.item)
                }
            }
            is SelectSavePointEvent.ClearMessage -> {
                _state.update { it.copy(message = null)}
            }
            is SelectSavePointEvent.ClearUiEvent -> {
                _state.update { it.copy(uiEvent = null)}
            }
        }
    }

    private fun initDropdownState(destinationIds: List<Int>){
        viewModelScope.launch {
            val menuItems = SelectSavePointMenuItem.fromDestinationIds(destinationIds,addAll = true)
            val useMenu = menuItems.size > 1
            _state.update { state->
                state.copy(
                    dropdownItems = menuItems,
                    showDropdownMenu = useMenu,
                    selectedDropdownItem = menuItems.firstOrNull()
                )
            }
            menuItems.firstOrNull()?.let { filterFlow.emit(it) }
        }
    }

    private fun loadData(){
        initDropdownState(args.destinationFilters)
        _state.update { it.copy(title = args.title)}

        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {

            combine(filterFlow,savePointsUseCases.getSavePoints(listOf(args.typeId))){ filter, savePoints->
                if(!_state.value.showDropdownMenu) return@combine savePoints

                if(filter == SelectSavePointMenuItem.All)
                    return@combine savePoints
                savePoints.filter { filter.destinationId == it.savePointDestination.destinationId }
            }.collectLatest {items->
                _state.update { it.copy(savePoints = items)}
            }
        }
    }

    private fun loadSavePoint(){
        viewModelScope.launch {
            _state.value.currentSelectedSavePoint?.let { savePoint ->
                val catEnum = savePoint.savePointDestination.type.toCategoryEnum() ?: CategoryEnum.AllDict
                val uiEvent = when(val destination = savePoint.savePointDestination){
                    is SavePointDestination.CategoryAll->{
                        SelectSavePointUiEvent.NavigateToCategoryAll(
                            catEnum, savePoint.itemPosIndex
                        )
                    }
                    is SavePointDestination.CategoryRandom->{
                        SelectSavePointUiEvent.NavigateToCategoryRandom(
                            catEnum, savePoint.itemPosIndex
                        )
                    }
                    is SavePointDestination.CategoryAlphabetic->{
                        SelectSavePointUiEvent.NavigateToCategoryAlphabetic(
                            catEnum, destination.c, savePoint.itemPosIndex
                        )
                    }
                    is SavePointDestination.List->{
                        SelectSavePointUiEvent.NavigateToList(
                            destination.listId, savePoint.itemPosIndex
                        )
                    }
                }
                _state.update { it.copy(uiEvent = uiEvent)}
            }
        }
    }

}



















package com.masterplus.trdictionary.features.savepoint.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.use_cases.savepoint.SavePointsUseCases
import com.masterplus.trdictionary.core.domain.util.UiText
import com.masterplus.trdictionary.features.savepoint.presentation.constants.SelectSavePointMenuItem
import com.masterplus.trdictionary.features.savepoint.presentation.navigation.SelectSavePointArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectSavePointViewModel @Inject constructor(
    private val savePointsUseCases: SavePointsUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val args = SelectSavePointArgs(savedStateHandle)

    var state by mutableStateOf(SelectSavePointState())
        private set

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
                    state = state.copy(message = UiText.Resource(R.string.successfully_deleted))
                }
            }
            is SelectSavePointEvent.EditTitle -> {
                viewModelScope.launch {
                    savePointsUseCases.updateSavePoint(event.savePoint.copy(title = event.title))
                    state = state.copy(message = UiText.Resource(R.string.successfully_updated))
                }
            }
            is SelectSavePointEvent.LoadSavePoint -> {
                loadSavePoint()
            }
            is SelectSavePointEvent.Select -> {
                state = state.copy(selectedSavePoint = event.savePoint)
            }
            is SelectSavePointEvent.ShowDialog -> {
                state = state.copy(
                    showDialog = event.showDialog,
                    modalDialog = event.dialogEvent
                )
            }
            is SelectSavePointEvent.SelectDropdownMenuItem -> {
                viewModelScope.launch {
                    state = state.copy(
                        selectedDropdownItem = event.item
                    )
                    filterFlow.emit(event.item)
                }
            }
            is SelectSavePointEvent.ClearMessage -> {
                state = state.copy(message = null)
            }
            is SelectSavePointEvent.ClearUiEvent -> {
                state = state.copy(uiEvent = null)
            }
        }
    }

    private fun initDropdownState(destinationIds: List<Int>){
        viewModelScope.launch {
            val menuItems = SelectSavePointMenuItem.fromDestinationIds(destinationIds,addAll = true)
            val useMenu = menuItems.size > 1

            state = state.copy(
                dropdownItems = menuItems,
                showDropdownMenu = useMenu,
                selectedDropdownItem = menuItems.firstOrNull()
            )
            menuItems.firstOrNull()?.let { filterFlow.emit(it) }
        }
    }

    private fun loadData(){
        initDropdownState(args.destinationFilters)
        state = state.copy(title = args.title)

        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {

            combine(filterFlow,savePointsUseCases.getSavePoints(listOf(args.typeId))){ filter, savePoints->
                if(!state.showDropdownMenu) return@combine savePoints

                if(filter == SelectSavePointMenuItem.All)
                    return@combine savePoints
                savePoints.filter { filter.destinationId == it.savePointDestination.destinationId }
            }.collectLatest {items->
                state = state.copy(
                    savePoints = items
                )
            }
        }
    }

    private fun loadSavePoint(){
        viewModelScope.launch {
            state.selectedSavePoint?.let { savePoint ->
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
                state = state.copy(uiEvent = uiEvent)
            }
        }
    }

}



















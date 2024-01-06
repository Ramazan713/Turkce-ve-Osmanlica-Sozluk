package com.masterplus.trdictionary.features.search.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.features.search.domain.constants.SearchKind
import com.masterplus.trdictionary.features.search.domain.repo.HistoryRepo
import com.masterplus.trdictionary.features.search.domain.repo.SearchRepo
import com.masterplus.trdictionary.features.search.presentation.navigation.SearchArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val historyRepo: HistoryRepo,
    private val searchRepo: SearchRepo,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val searchArgs = SearchArgs(savedStateHandle)

    private var historyLoadJob: Job? = null
    private var searchResultJob: Job? = null

    private val queryFilterFlow = MutableSharedFlow<String>(replay = 1)
    private val categoryFilterFlow = MutableSharedFlow<CategoryEnum>(replay = 1)
    private val searchKindFilterFlow = MutableSharedFlow<SearchKind>(replay = 1)

    private val combinedDataFlow = combine(
        queryFilterFlow,
        searchKindFilterFlow,
        categoryFilterFlow
    ){ query, searchKindFilter, categoryFilter->
        SearchModel(query,categoryFilter,searchKindFilter)
    }


    init {
        setDefaultFilter()
        listenSearchResult()
        listenHistories()

    }

    fun onEvent(event: SearchEvent){
        when(event){
            is SearchEvent.ClearQuery -> {
                search("")
            }
            is SearchEvent.SearchQuery -> {
                event.queryText?.let { search(it) }
                event.query?.let { search(it) }
            }
            is SearchEvent.DeleteHistory -> {
                viewModelScope.launch {
                    historyRepo.deleteHistory(event.history)
                }
            }
            is SearchEvent.HistoryClicked -> {
                search(event.history.content)
            }
            is SearchEvent.ChangeFilter -> {
                changeFilter(event)
            }
            is SearchEvent.ShowDialog -> {
                _state.update { it.copy(dialogEvent = event.dialogEvent)}
            }
            SearchEvent.HideDetail -> {
                _state.update { state-> state.copy(
                    isDetailOpen = false,
                    selectedWordId = null
                )}
            }
            is SearchEvent.ShowDetail -> {
                _state.update { state-> state.copy(
                    isDetailOpen = true,
                    selectedWordId = event.wordWithSimilar.wordId
                )}
            }
        }
    }

    private fun setDefaultFilter(){
        viewModelScope.launch {
            val cat = CategoryEnum.fromCatId(searchArgs.catId)
            _state.update { state-> state.copy(
                defaultCategory = cat,
                categoryFilter = cat
            )}
            searchKindFilterFlow.emit(_state.value.searchKind)
            categoryFilterFlow.emit(cat)
        }
    }

    private fun search(query: String){
        search(_state.value.query.copy(text = query, selection = TextRange(query.length)))
    }

    private fun search(query: TextFieldValue){
        viewModelScope.launch {
            val text = query.text
            if(text != _state.value.query.text){
                _state.update { it.copy(query = query, selectedWordId = null)}
                queryFilterFlow.emit(text)
            }
        }
    }

    private fun changeFilter(event: SearchEvent.ChangeFilter){
        viewModelScope.launch {
            _state.update { state->
                state.copy(
                    categoryFilter = event.catEnum,
                    searchKind = event.searchKind,
                    badge = findBadgeCount(event.catEnum,event.searchKind)
                )
            }
            searchKindFilterFlow.emit(event.searchKind)
            categoryFilterFlow.emit(event.catEnum)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun listenSearchResult(){

        searchResultJob?.cancel()
        searchResultJob = viewModelScope.launch{
            combinedDataFlow
            .distinctUntilChanged()
            .debounce {
                _state.update { it.copy(searchLoading = true)}
                K.searchDelayMilliSeconds
            }.flatMapLatest { searchModel->

                val query = searchModel.query
                if (query.isBlank() || query.length == 1){
                    _state.update { state->
                        state.copy(
                            wordResults = emptyList(),
                            selectedWordId = null,
                            searchLoading = false
                        )
                    }
                    return@flatMapLatest emptyFlow<List<WordWithSimilar>>()
                }
                historyRepo.insertOrUpdateHistory(query)

                searchRepo.search(query,searchModel.categoryEnum,searchModel.searchKind)
            }.collectLatest { searchResults->
                _state.update { state->
                    state.copy(
                        wordResults = searchResults,
                        searchLoading = false,
                        selectedWordId = state.selectedWordId ?: searchResults.firstOrNull()?.wordId
                    )
                }
            }
        }
    }

    private fun listenHistories(){
        historyLoadJob?.cancel()
        historyLoadJob = viewModelScope.launch {
            historyRepo.getFlowHistories().collectLatest { histories->
                _state.update { it.copy(histories = histories)}
            }
        }
    }


    private fun findBadgeCount(categoryEnum: CategoryEnum,searchKind: SearchKind): String?{
        var badgeCount = 0
        if(searchKind != SearchKind.Word){
            badgeCount += 1
        }

        if(categoryEnum != _state.value.defaultCategory){
            badgeCount += 1
        }

        return if(badgeCount!=0) badgeCount.toString() else null
    }
}

private data class SearchModel(
    val query: String,
    val categoryEnum: CategoryEnum,
    val searchKind: SearchKind
)
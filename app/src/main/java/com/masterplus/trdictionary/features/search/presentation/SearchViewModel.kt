package com.masterplus.trdictionary.features.search.presentation

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val historyRepo: HistoryRepo,
    private val searchRepo: SearchRepo,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val searchArgs = SearchArgs(savedStateHandle)

    init {
        init()
    }

    fun onEvent(event: SearchEvent){
        when(event){
            is SearchEvent.ClearQuery -> {
                _state.update { it.copyAndSetQuery("")}
            }
            is SearchEvent.SetTextFieldValue -> {
                _state.update { it.copy(
                    queryFieldValue = event.textFieldValue
                ) }
            }
            is SearchEvent.SearchQuery -> {
                _state.update { it.copyAndSetQuery(event.query)}
            }
            is SearchEvent.DeleteHistory -> {
                viewModelScope.launch {
                    historyRepo.deleteHistory(event.history)
                }
            }
            is SearchEvent.HistoryClicked -> {
                _state.update { it.copyAndSetQuery(event.history.content) }
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

            is SearchEvent.HasFocusChange -> {
                _state.update { it.copy(
                    hasSearchFocus = event.hasFocus
                ) }
            }

            SearchEvent.ClearUIEvent -> {
                _state.update { it.copy(
                    searchUiEvent = null
                ) }
            }
            SearchEvent.InsertHistoryBeforeNavigateUp -> {
                viewModelScope.launch {
                    val query = getValidQueryOrNull(_state.value.query)
                    if(query != null){
                        historyRepo.insertOrUpdateHistory(query)
                    }
                    _state.update { it.copy(
                        searchUiEvent = SearchUiEvent.NavigateBack
                    ) }
                }
            }

            is SearchEvent.AddHistory -> {
                viewModelScope.launch {
                    if(isValidQuery(event.historyName)){
                        historyRepo.insertOrUpdateHistory(event.historyName)
                    }
                }
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
        }
    }


    private fun init(){
        setDefaultFilter()
        listenHistories()
        listenSearchResult()
        listenAddingHistory()
        listenSelectedWordNullable()
    }

    private fun listenHistories(){
        historyRepo
            .getFlowHistories()
            .onEach { histories ->
                _state.update { it.copy(histories = histories)}
            }
            .launchIn(viewModelScope)
    }


    private fun listenSelectedWordNullable(){
        _state
            .map { it.query }
            .distinctUntilChanged()
            .onEach {
                _state.update {
                    it.copy(selectedWordId = null)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun listenAddingHistory(){
        _state
            .map {
                if(it.hasSearchFocus) return@map ""
                it.query
            }
            .debounce(K.searchDelayMilliSeconds)
            .distinctUntilChanged()
            .filter { query ->
                isValidQuery(query)
            }
            .onEach {query ->
                historyRepo.insertOrUpdateHistory(query)
            }
            .launchIn(viewModelScope)

        _state
            .map { it.query }
            .debounce(K.searchLastDelayMilliSeconds)
            .distinctUntilChanged()
            .filter { query ->
                isValidQuery(query)
            }
            .onEach { query ->
                historyRepo.insertOrUpdateHistory(query)
            }
            .launchIn(viewModelScope)
    }

    private fun listenSearchResult(){
        _state
            .distinctUntilChanged { old, new ->
                old.equalSearchValue(new)
            }
            .debounce {
                _state.update { it.copy(searchLoading = true)}
                K.searchDelayMilliSeconds
            }
            .flatMapLatest { searchState ->
                val query = searchState.query
                if (!isValidQuery(query)){
                    _state.update { state->
                        state.copy(
                            wordResults = emptyList(),
                            selectedWordId = null,
                            searchLoading = false
                        )
                    }
                    return@flatMapLatest emptyFlow<List<WordWithSimilar>>()
                }

                searchRepo.search(query,searchState.categoryFilter,searchState.searchKind)
            }
            .onEach { searchResults ->
                _state.update { state->
                    state.copy(
                        wordResults = searchResults,
                        searchLoading = false,
                        selectedWordId = state.selectedWordId ?: searchResults.firstOrNull()?.wordId
                    )
                }
            }.launchIn(viewModelScope)
    }


    private fun findBadgeCount(categoryEnum: CategoryEnum,searchKind: SearchKind): String?{
        var badgeCount = 0
        if(searchKind != SearchKind.Word){
            badgeCount += 1
        }

        if(categoryEnum != _state.value.defaultCategory){
            badgeCount += 1
        }

        return if(badgeCount != 0) badgeCount.toString() else null
    }


    private fun getValidQueryOrNull(query: String): String? {
        if(isValidQuery(query)) return query
        return null
    }

    private fun isValidQuery(query: String): Boolean{
        return query.isNotBlank() && query.length > 1
    }

}

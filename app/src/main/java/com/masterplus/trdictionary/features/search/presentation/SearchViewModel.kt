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
import com.masterplus.trdictionary.features.search.domain.constants.SearchKind
import com.masterplus.trdictionary.features.search.domain.repo.HistoryRepo
import com.masterplus.trdictionary.features.search.domain.repo.SearchRepo
import com.masterplus.trdictionary.features.search.presentation.navigation.SearchArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val historyRepo: HistoryRepo,
    private val searchRepo: SearchRepo,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    var state by mutableStateOf(SearchState())
        private set

    private val searchArgs = SearchArgs(savedStateHandle)

    private var historyLoadJob: Job? = null

    private var searchJob: Job? = null

    init {
        loadHistories()
        setDefaultFilter()
    }


    fun onEvent(event: SearchEvent){
        when(event){
            is SearchEvent.ChangeQuery -> {
                setQuery(query = event.query)
            }
            is SearchEvent.DeleteHistory -> {
                viewModelScope.launch {
                    historyRepo.deleteHistory(event.history)
                }
            }
            is SearchEvent.HistoryClicked -> {
                navigateToDetailWord(event.history.content,event.history.wordId,true)
                setQuery("")
            }
            is SearchEvent.NavigateToDetailWord -> {
                navigateToDetailWord(event.content,event.wordId,false)
            }
            is SearchEvent.SetQuery -> {
                setQuery(event.query)
            }
            is SearchEvent.ChangeFilter -> {
                changeFilter(event)
            }

            is SearchEvent.ShowDialog -> {
                state = state.copy(showDialog = event.showDialog, dialogEvent = event.dialogEvent)
            }
            SearchEvent.ClearUiEvent -> {
                state = state.copy(searchUiEvent = null)
            }
        }
    }

    private fun setDefaultFilter(){
        val cat = CategoryEnum.fromCatId(searchArgs.catId)
        state = state.copy(
            defaultCategory = cat,
            categoryFilter = cat
        )
    }

    private fun changeFilter(event: SearchEvent.ChangeFilter){
        state = state.copy(
            categoryFilter = event.catEnum,
            searchKind = event.searchKind,
            badge = findBadgeCount(event.catEnum,event.searchKind)
        )
        searchQuerySafe(state.query.text)
    }


    private fun setQuery(query: String){
        setQuery(state.query.copy(text = query, selection = TextRange(query.length)))
    }

    private fun setQuery(query: TextFieldValue){
        state = state.copy(
            query = query
        )
        searchQuerySafe(query.text)
    }

    private fun searchQuerySafe(query: String){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(K.searchDelayMilliSeconds)
            search(query)
        }
    }

    private fun search(query: String){
        if (query.isBlank() || query.length == 1){
            state = state.copy(wordResults = emptyList())
            return
        }
        viewModelScope.launch {
            state = state.copy(searchLoading = true)
            val result = searchRepo.searchSimple(query.trim(), state.categoryFilter, state.searchKind)
            state = state.copy(wordResults = result, searchLoading = false)
        }
    }

    private fun navigateToDetailWord(content: String, wordId: Int,popCurrentPage: Boolean){
        viewModelScope.launch {
            historyRepo.insertOrUpdateHistory(content,wordId)
            state = state.copy(searchUiEvent = SearchUiEvent.NavigateToDetailWord(wordId,popCurrentPage))
        }
    }

    private fun loadHistories(){
        historyLoadJob?.cancel()
        historyLoadJob = viewModelScope.launch {
            historyRepo.getFlowHistories().collectLatest {
                state = state.copy(histories = it)
            }
        }
    }

    private fun findBadgeCount(categoryEnum: CategoryEnum,searchKind: SearchKind): String?{
        var badgeCount = 0
        if(searchKind != SearchKind.Word){
            badgeCount += 1
        }

        if(categoryEnum != state.defaultCategory){
            badgeCount += 1
        }

        return if(badgeCount!=0) badgeCount.toString() else null
    }

}
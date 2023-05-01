package com.masterplus.trdictionary.features.search.presentation

sealed class SearchUiEvent{
    data class NavigateToDetailWord(val wordId: Int, val popCurrentPage: Boolean): SearchUiEvent()
}

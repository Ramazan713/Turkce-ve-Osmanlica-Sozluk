package com.masterplus.trdictionary.features.search.presentation

import androidx.compose.ui.text.input.TextFieldValue
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.features.search.domain.constants.SearchKind
import com.masterplus.trdictionary.features.search.domain.model.History

sealed class SearchEvent{
    data class ChangeQuery(val query: TextFieldValue): SearchEvent()

    data class SetQuery(val query: String): SearchEvent()

    data class DeleteHistory(val history: History): SearchEvent()

    data class HistoryClicked(val history: History): SearchEvent()

    data class NavigateToDetailWord(val content: String,val wordId: Int): SearchEvent()

    data class ChangeFilter(val catEnum: CategoryEnum, val searchKind: SearchKind): SearchEvent()

    data class ShowDialog(val showDialog: Boolean, val dialogEvent: SearchDialogEvent? = null): SearchEvent()

    object ClearUiEvent: SearchEvent()
}

package com.masterplus.trdictionary.features.search.presentation

import androidx.compose.ui.text.input.TextFieldValue
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.features.search.domain.constants.SearchKind
import com.masterplus.trdictionary.features.search.domain.model.History

sealed class SearchEvent{

    data object ClearQuery: SearchEvent()
    data class SearchQuery(
        val query: TextFieldValue? = null,
        val queryText: String? = null
    ): SearchEvent()

    data class DeleteHistory(val history: History): SearchEvent()

    data class HistoryClicked(val history: History): SearchEvent()

    data class ChangeFilter(val catEnum: CategoryEnum, val searchKind: SearchKind): SearchEvent()

    data class ShowDialog(val dialogEvent: SearchDialogEvent? = null): SearchEvent()

    data object HideDetail: SearchEvent()

    data class ShowDetail(
        val wordWithSimilar: WordWithSimilar
    ): SearchEvent()
}

package com.masterplus.trdictionary.features.search.presentation

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.features.search.domain.constants.SearchKind
import com.masterplus.trdictionary.features.search.domain.model.History

data class SearchState(
    val query: TextFieldValue = TextFieldValue("", selection = TextRange.Zero),
    val searchLoading: Boolean = false,
    val categoryFilter: CategoryEnum = CategoryEnum.AllDict,
    val defaultCategory: CategoryEnum = CategoryEnum.AllDict,
    val searchKind: SearchKind = SearchKind.Word,
    val badge: String? = null,
    val histories: List<History> = emptyList(),
    val wordResults: List<WordWithSimilar> = emptyList(),
    val isDetailOpen: Boolean = false,
    val selectedWordId: Int? = null,
    val dialogEvent: SearchDialogEvent? = null,
    val searchUiEvent: SearchUiEvent? = null
){

    val selectedWord: WordWithSimilar? get() {
        return wordResults.firstOrNull { it.wordId == selectedWordId }
    }

}

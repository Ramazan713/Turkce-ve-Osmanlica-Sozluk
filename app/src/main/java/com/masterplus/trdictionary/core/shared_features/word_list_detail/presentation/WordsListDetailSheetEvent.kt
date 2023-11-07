package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation

import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings

sealed interface WordsListDetailSheetEvent{

    data class ShowSelectBottomMenu(
        val word: WordDetailMeanings,
        val pos: Int,
        val savePointDestination: SavePointDestination,
        val listIdControl: Int?,
    ): WordsListDetailSheetEvent

    data class ShowSelectList(
        val wordId: Int,
        val listIdControl: Int?,
    ): WordsListDetailSheetEvent
}
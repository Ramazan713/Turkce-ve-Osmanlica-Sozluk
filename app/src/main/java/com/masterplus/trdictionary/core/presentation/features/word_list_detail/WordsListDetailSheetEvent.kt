package com.masterplus.trdictionary.core.presentation.features.word_list_detail

import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.features.word_detail.domain.model.WordDetailMeanings

sealed interface WordsListDetailSheetEvent{

    data class ShowSelectBottomMenu(
        val word: WordDetailMeanings,
        val pos: Int,
        val savePointDestination: SavePointDestination,
        val listIdControl: Int? = null,
    ): WordsListDetailSheetEvent

    data class ShowSelectList(
        val wordId: Int,
    ): WordsListDetailSheetEvent
}
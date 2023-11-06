package com.masterplus.trdictionary.core.presentation.features.word_list_detail

import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.model.Word
import com.masterplus.trdictionary.features.word_detail.domain.model.WordDetailMeanings

sealed interface WordsListDetailDialogEvent {

    data class ShowSelectNumber(
        val itemCount: Int,
        val currentPos: Int
    ): WordsListDetailDialogEvent

    data class ShowProverbIdiomsWordsList(
        val wordDetailMeanings: WordDetailMeanings,
    ): WordsListDetailDialogEvent

    data class ShowCompoundWordsList(
        val wordDetailMeanings: WordDetailMeanings,
    ): WordsListDetailDialogEvent

    data class EditSavePoint(
        val savePointDestination: SavePointDestination,
        val shortTitle: String,
        val pos: Int? = null,
    ): WordsListDetailDialogEvent


    data class ShowShareDialog(
        val savePointDestination: SavePointDestination,
        val savePointType: SavePointType?,
        val pos: Int?,
        val word: Word,
    ): WordsListDetailDialogEvent

}
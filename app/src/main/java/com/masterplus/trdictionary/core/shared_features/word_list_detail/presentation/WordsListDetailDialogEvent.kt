package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation

import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetail
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings

sealed interface WordsListDetailDialogEvent {

    data class AskFavoriteDelete(
        val wordId: Int
    ): WordsListDetailDialogEvent

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
        val word: WordDetail,
    ): WordsListDetailDialogEvent

}
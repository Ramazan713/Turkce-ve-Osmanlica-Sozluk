package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation

import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.shared_features.share.domain.enums.ShareItemEnum
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetail
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar


sealed interface WordsListDetailEvent{

    data object ClearMessage: WordsListDetailEvent

    data object ClearNavigateToPos: WordsListDetailEvent

    data object ClearShareResult: WordsListDetailEvent

    data class ShareWord(
        val shareItem: ShareItemEnum,
        val wordDetail: WordDetail
    ): WordsListDetailEvent

    data class HideSelectedWords(
        val lastPos: Int?
    ): WordsListDetailEvent

    data class ShowSelectedWords(
        val word: WordWithSimilar,
        val pos: Int
    ): WordsListDetailEvent

    data class NavigateToPos(
        val pos: Int
    ): WordsListDetailEvent


    data class ShowDialog(
        val dialogEvent: WordsListDetailDialogEvent? = null,
    ): WordsListDetailEvent

    data class ShowSheet(
        val sheetEvent: WordsListDetailSheetEvent? = null,
    ): WordsListDetailEvent

    data class AddFavorite(
        val wordId: Int,
        val listIdControl: Int? = null,
        val inFavorite: Boolean? = null
    ): WordsListDetailEvent

    data class AddListWords(val listView: ListView, val wordId: Int): WordsListDetailEvent

    data class ListenWords(val word: WordDetail): WordsListDetailEvent

}
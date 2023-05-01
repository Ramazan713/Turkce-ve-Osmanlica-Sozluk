package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared

import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.domain.model.Word
import com.masterplus.trdictionary.features.word_detail.domain.constants.ShareItemEnum

sealed class WordsDetailSharedEvent{

    data class AddFavorite(val wordId: Int): WordsDetailSharedEvent()

    data class AddListWord(val listView: ListView, val wordId: Int): WordsDetailSharedEvent()

    data class ShowModal(val showModal: Boolean, val modalEvent: WordsDetailSharedModalEvent? = null): WordsDetailSharedEvent()

    data class ShowDialog(val showDialog: Boolean, val dialogEvent: WordsDetailSharedDialogEvent? = null): WordsDetailSharedEvent()

    data class ListenWord(val word: Word): WordsDetailSharedEvent()

    data class EvaluateShareWord(
        val savePointDestination: SavePointDestination,
        val savePointType: SavePointType?,
        val pos: Int?,
        val wordId: Int,
        val wordRandomOrder: Int,
        val shareItemEnum: ShareItemEnum
    ): WordsDetailSharedEvent()

    object ClearUiEvent: WordsDetailSharedEvent()

}

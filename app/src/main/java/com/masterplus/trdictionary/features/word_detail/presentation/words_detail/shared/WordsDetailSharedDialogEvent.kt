package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared

import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult


sealed interface WordsDetailSharedDialogEvent{

    object ShowSelectNumber: WordsDetailSharedDialogEvent

    data class ShowProverbIdiomsWords(val words: List<SimpleWordResult>): WordsDetailSharedDialogEvent

    data class ShowCompoundWords(val words: List<SimpleWordResult>): WordsDetailSharedDialogEvent

    data class EditSavePoint(
        val savePointDestination: SavePointDestination,
        val shortTitle: String,
        val pos: Int? = null
    ): WordsDetailSharedDialogEvent


}


sealed class WordDetailListDialogEvent: WordsDetailSharedDialogEvent{
    data class AskUnFavorite(val wordId: Int): WordDetailListDialogEvent()
}


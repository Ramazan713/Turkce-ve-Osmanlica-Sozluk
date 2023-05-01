package com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared

import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.model.Word
import com.masterplus.trdictionary.features.word_detail.domain.constants.ShareItemEnum

sealed class WordListSharedEvent{
    data class ShowDialog(val showDialog: Boolean, val dialogEvent: WordListSharedDialogEvent? = null): WordListSharedEvent()

    data class ShowModal(val showModal: Boolean, val modalEvent: WordListSharedModalEvent? = null): WordListSharedEvent()

    data class EvaluateShareWord(
        val savePointDestination: SavePointDestination,
        val savePointType: SavePointType?,
        val pos: Int?,
        val word: Word,
        val shareItemEnum: ShareItemEnum
    ): WordListSharedEvent()

    object ClearUiEvent: WordListSharedEvent()
}

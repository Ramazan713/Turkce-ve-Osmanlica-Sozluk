package com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared

import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.model.Word

sealed class WordListSharedDialogEvent {
    object ShowSelectNumber: WordListSharedDialogEvent()

    data class EditSavePoint(
        val savePointDestination: SavePointDestination,
        val shortTitle: String,
        val pos: Int? = null
    ): WordListSharedDialogEvent()

    data class ShowShareDialog(
        val savePointDestination: SavePointDestination,
        val savePointType: SavePointType?,
        val pos: Int?,
        val word: Word
    ): WordListSharedDialogEvent()

}


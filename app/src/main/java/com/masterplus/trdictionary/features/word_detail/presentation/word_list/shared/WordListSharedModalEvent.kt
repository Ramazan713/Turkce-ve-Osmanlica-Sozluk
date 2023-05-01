package com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared

import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult

sealed class WordListSharedModalEvent{
    data class ShowSelectBottomMenu(
        val simpleWord: SimpleWordResult,
        val pos: Int,
        val savePointType: SavePointType?,
        val savePointDestination: SavePointDestination
    ): WordListSharedModalEvent()
}

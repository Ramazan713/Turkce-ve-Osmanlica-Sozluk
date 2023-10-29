package com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail

import com.masterplus.trdictionary.features.word_detail.domain.model.AudioState
import com.masterplus.trdictionary.features.word_detail.domain.model.WordDetailMeanings

data class SingleWordDetailState(
        val isLoading: Boolean = false,
        val audioState: AudioState = AudioState(),
        val allWords: List<WordDetailMeanings> = emptyList(),
        val showDialog: Boolean = false,
        val dialogEvent: SingleWordDetailDialogEvent? = null,
        val showModal: Boolean = false,
        val modalEvent: SingleWordDetailModalEvent? = null
)

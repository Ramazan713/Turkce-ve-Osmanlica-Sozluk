package com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail

import com.masterplus.trdictionary.features.word_detail.domain.model.WordDetailInfoModel
import com.masterplus.trdictionary.features.word_detail.domain.model.AudioState

data class SingleWordDetailState(
    val isLoading: Boolean = false,
    val audioState: AudioState = AudioState(),
    val wordModels: List<WordDetailInfoModel> = emptyList(),
    val showDialog: Boolean = false,
    val dialogEvent: SingleWordDetailDialogEvent? = null,
    val showModal: Boolean = false,
    val modalEvent: SingleWordDetailModalEvent? = null
)

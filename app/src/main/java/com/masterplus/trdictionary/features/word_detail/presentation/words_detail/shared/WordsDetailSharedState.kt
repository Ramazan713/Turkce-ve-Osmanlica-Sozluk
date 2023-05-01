package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared


data class WordsDetailSharedState(
    val showDialog: Boolean = false,
    val dialogEvent: WordsDetailSharedDialogEvent? = null,
    val showModal: Boolean = false,
    val modalEvent: WordsDetailSharedModalEvent? = null,
    val uiEvent: WordsDetailSharedUiEvent? = null
)

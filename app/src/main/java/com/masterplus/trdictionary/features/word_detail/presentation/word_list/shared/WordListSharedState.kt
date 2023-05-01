package com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared

data class WordListSharedState(
    val showDialog: Boolean = false,
    val dialogEvent: WordListSharedDialogEvent? = null,
    val showModal: Boolean = false,
    val modalEvent: WordListSharedModalEvent? = null,
    val uiEvent: WordListSharedUiEvent? = null,
)

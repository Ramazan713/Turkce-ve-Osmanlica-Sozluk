package com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail

import com.masterplus.trdictionary.core.domain.model.SimpleWordResult

sealed class SingleWordDetailDialogEvent{

    data class ShowProverbIdiomsWords(val words: List<SimpleWordResult>): SingleWordDetailDialogEvent()

    data class ShowCompoundWords(val words: List<SimpleWordResult>): SingleWordDetailDialogEvent()



}

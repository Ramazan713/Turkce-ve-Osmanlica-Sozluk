package com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail

sealed class SingleWordDetailModalEvent{

    data class ShowSelectList(val wordId: Int): SingleWordDetailModalEvent()

}

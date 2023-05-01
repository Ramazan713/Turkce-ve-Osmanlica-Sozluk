package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared

sealed class WordsDetailSharedModalEvent{

    data class ShowSelectList(val wordId: Int): WordsDetailSharedModalEvent()

}

package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_list

sealed class WordsDetailListEvent{

    data class AddOrAskFavorite(val wordId: Int): WordsDetailListEvent()
}

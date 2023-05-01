package com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail

import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.domain.model.Word

sealed class SingleWordDetailEvent{

    data class AddFavorite(val wordId: Int): SingleWordDetailEvent()

    data class AddListWord(val listView: ListView, val wordId: Int): SingleWordDetailEvent()

    data class ShowModal(val showModal: Boolean, val modalEvent: SingleWordDetailModalEvent? = null): SingleWordDetailEvent()

    data class ShowDialog(val showDialog: Boolean, val dialogEvent: SingleWordDetailDialogEvent? = null): SingleWordDetailEvent()

    data class ListenWord(val word: Word): SingleWordDetailEvent()

}

package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_list

import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.model.ListModel
import com.masterplus.trdictionary.features.word_detail.domain.model.AudioState

data class WordsDetailListState(
    val listModel: ListModel? = null,
    val audioState: AudioState = AudioState(),
    val savePointDestination: SavePointDestination = SavePointDestination.List(1)
){
    val listName get() = listModel?.name?:""
}

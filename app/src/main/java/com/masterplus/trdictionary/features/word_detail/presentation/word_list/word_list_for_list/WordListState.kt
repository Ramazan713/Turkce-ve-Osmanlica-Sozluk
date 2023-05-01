package com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_for_list

import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.model.ListModel

data class WordListState(
    val list: ListModel? = null,
    val savePointDestination: SavePointDestination = SavePointDestination.List(1)
){

    val listName get() = list?.name ?: ""
}

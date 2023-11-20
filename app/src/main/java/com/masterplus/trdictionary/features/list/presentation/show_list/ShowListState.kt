package com.masterplus.trdictionary.features.list.presentation.show_list

import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.domain.utils.UiText


data class ShowListState(
    val items: List<ListView> = emptyList(),
    val message: UiText? = null,
    val showDialog: Boolean = false,
    val dialogEvent: ShowListDialogEvent? = null,
    val showModal: Boolean = false,
    val modalEvent: ShowListModelEvent? = null
)

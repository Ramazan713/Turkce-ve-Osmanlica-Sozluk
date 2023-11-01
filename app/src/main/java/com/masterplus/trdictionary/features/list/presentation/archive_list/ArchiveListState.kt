package com.masterplus.trdictionary.features.list.presentation.archive_list

import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.util.UiText

data class ArchiveListState(
    val items: List<ListView> = emptyList(),
    val message: UiText? = null,
    val showDialog: Boolean = false,
    val dialogEvent: ArchiveListDialogEvent? = null,
    val showModal: Boolean = false,
)

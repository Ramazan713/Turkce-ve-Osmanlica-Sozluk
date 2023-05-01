package com.masterplus.trdictionary.core.presentation.features.select_list.select_list_dia

import com.masterplus.trdictionary.features.list.domain.model.SelectableListView

data class SelectListState(
    val items: List<SelectableListView> = emptyList(),
    val listIdControl: Int? = null,
    val showDialog: Boolean = false,
    val dialogEvent: SelectListDialogEvent? = null
)

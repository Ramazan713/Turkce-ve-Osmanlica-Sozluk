package com.masterplus.trdictionary.core.presentation.features.select_list.select_list_dia

import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.features.list.domain.model.SelectableListView

sealed class SelectListEvent{

    data class LoadData(val wordId: Int, val listIdControl: Int?): SelectListEvent()

    data class NewList(val listName: String): SelectListEvent()

    data class AddToList(val wordId: Int, val listView: ListView): SelectListEvent()

    data class AddOrAskToList(val wordId: Int, val selectableListView: SelectableListView): SelectListEvent()

    data class ShowDialog(val showDialog: Boolean, val dialogEvent: SelectListDialogEvent? = null):
        SelectListEvent()
}

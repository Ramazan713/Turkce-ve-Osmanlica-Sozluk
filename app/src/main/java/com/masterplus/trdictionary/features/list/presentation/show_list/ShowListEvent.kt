package com.masterplus.trdictionary.features.list.presentation.show_list

import com.masterplus.trdictionary.core.domain.model.ListView

sealed class ShowListEvent{

    data class ShowDialog(val showDialog: Boolean,
                          val dialogEvent: ShowListDialogEvent? = null): ShowListEvent()

    data class ShowModal(val showModal: Boolean,
                         val modalEvent: ShowListModelEvent? = null): ShowListEvent()

    data class AddNewList(val listName: String): ShowListEvent()

    data class Rename(val listView: ListView, val newName: String): ShowListEvent()

    data class Copy(val listView: ListView): ShowListEvent()

    data class Archive(val listView: ListView): ShowListEvent()

    data class Delete(val listView: ListView): ShowListEvent()

    object ClearMessage: ShowListEvent()
}

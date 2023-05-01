package com.masterplus.trdictionary.features.list.presentation.show_list

import com.masterplus.trdictionary.core.domain.model.ListView

sealed class ShowListModelEvent{

    data class ShowSelectBottomMenu(val listView: ListView): ShowListModelEvent()
}

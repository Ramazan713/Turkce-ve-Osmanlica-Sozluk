package com.masterplus.trdictionary.features.list.domain.model

import com.masterplus.trdictionary.core.domain.model.ListView


data class SelectableListView(
    val listView: ListView,
    val isSelected: Boolean
)

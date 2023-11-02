package com.masterplus.trdictionary.core.util

import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.features.list.domain.model.SelectableListView

object SampleDatas {

    val listView1 = ListView(1,"list item 1",false,
        isArchive = false,
        listPos = 1,
        contentMaxPos = 1,
        itemCounts = 1
    )
    val listView2 = ListView(2,"list item 2",
        isRemovable = true,
        isArchive = false,
        listPos = 2,
        contentMaxPos = 1,
        itemCounts = 1
    )
    val listView3 = ListView(3,"list item 3",true,
        isArchive = false,
        listPos = 3,
        contentMaxPos = 1,
        itemCounts = 1
    )

    val selectableListView = SelectableListView(listView = listView1,true)
    val selectableListView2 = SelectableListView(listView = listView2,false)
    val selectableListView3 = SelectableListView(listView = listView3,false)

    val selectableListViewArr = listOf(selectableListView, selectableListView2, selectableListView3)

}
package com.masterplus.trdictionary.core.data.local.views

import androidx.room.DatabaseView

@DatabaseView(viewName = "listViews", value = """
     select L.id, L.name, L.isRemovable, L.isArchive, L.pos listPos,
     count(LW.wordId) itemCounts, ifnull(max(LW.pos),0) contentMaxPos 
     from Lists L left join  ListWords LW on  L.id = LW.listId
     group by L.id
""")
data class ListViewEntity(
    val id: Int?,
    val name: String,
    val isRemovable: Boolean,
    val isArchive: Boolean,
    val listPos: Int,
    val contentMaxPos: Int,
    val itemCounts: Int
)

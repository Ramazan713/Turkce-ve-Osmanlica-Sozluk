package com.masterplus.trdictionary.core.data.local.views

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "WordListsView",
    value = """
        select W.id wordId,
        (select exists(select * from listWords LW, lists L
          where LW.listId = L.id and L.isRemovable = 1 and LW.wordId = W.id)) inAnyList,
        (select exists(select * from listWords LW, lists L
          where LW.listId = L.id and L.isRemovable = 0 and LW.wordId = W.id)) inFavorite 
        from words W
    """
)
data class WordListsViewEntity(
    val wordId: Int,
    val inAnyList: Boolean,
    val inFavorite: Boolean
)

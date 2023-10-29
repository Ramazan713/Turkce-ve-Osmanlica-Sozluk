package com.masterplus.trdictionary.core.data.local.views

import androidx.room.DatabaseView


@DatabaseView(viewName = "WordDetailsView", value = """
    select
	(select exists(select * from listWords LW, lists L
	  where LW.listId = L.id and L.isRemovable = 1 and LW.wordId = W.id limit 1)) inAnyList,
	(select exists(select * from listWords LW, lists L
	  where LW.listId = L.id and L.isRemovable = 0 and LW.wordId = W.id limit 1)) inFavorite,
	  (select exists(select * from CompoundWords where wordId = W.id limit 1)) hasCompoundWords,
	  W.*
	from words W
""")
data class WordDetailView(
    val inAnyList: Boolean,
    val inFavorite: Boolean,
    val hasCompoundWords: Boolean,
    val id: Int,
    val prefix: String?,
    val word: String,
    val suffix: String?,
    val searchWord: String,
    val showInQuery: Int,
    val randomOrder: Int,
    val dictTypeId: Int,
    val wordTypeId: Int,
    val showTTS: Boolean
)

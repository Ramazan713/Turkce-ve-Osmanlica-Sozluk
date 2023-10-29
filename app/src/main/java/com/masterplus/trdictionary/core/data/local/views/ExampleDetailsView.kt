package com.masterplus.trdictionary.core.data.local.views

import androidx.room.DatabaseView


@DatabaseView(viewName = "ExampleDetailsView", value = """
    select E.*, A.name authorName from Examples E, Authors A 
    where E.authorId = A.id
""")
data class ExampleDetailsView(
    val id: Int,
    val meaningId: Int,
    val authorId: Int,
    val orderItem: Int,
    val content: String,
    val authorName: String
)

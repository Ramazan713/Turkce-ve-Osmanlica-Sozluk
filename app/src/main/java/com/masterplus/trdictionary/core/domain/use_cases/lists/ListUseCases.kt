package com.masterplus.trdictionary.core.domain.use_cases.lists

data class ListUseCases(
    val insertList: InsertList,
    val updateList: UpdateList,
    val deleteList: DeleteList,
    val getLists: GetLists,
    val copyList: CopyList,
)

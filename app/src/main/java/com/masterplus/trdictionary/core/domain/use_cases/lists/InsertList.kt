package com.masterplus.trdictionary.core.domain.use_cases.lists

import com.masterplus.trdictionary.core.domain.model.ListModel
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import javax.inject.Inject

class InsertList @Inject constructor(
    private val listRepo: ListRepo
) {

    suspend fun invoke(listName: String): Int{
        val pos = listRepo.getMaxPos() + 1
        val listModel = ListModel(
            name = listName,
            pos = pos,
            isRemovable = true,
            isArchive = false,
            id = null
        )
        return listRepo.insertList(listModel).toInt()
    }
}
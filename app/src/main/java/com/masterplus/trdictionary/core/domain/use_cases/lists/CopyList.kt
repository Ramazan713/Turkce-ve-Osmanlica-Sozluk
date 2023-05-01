package com.masterplus.trdictionary.core.domain.use_cases.lists

import com.masterplus.trdictionary.core.data.local.mapper.toListModel
import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.domain.repo.ListWordsRepo
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import javax.inject.Inject

class CopyList @Inject constructor(
    private val listRepo: ListRepo,
    private val listWordsRepo: ListWordsRepo,
){

    suspend fun invoke(listView: ListView){
        val pos = listRepo.getMaxPos() + 1

        val listModel = listView.toListModel()
            .copy(
                pos = pos,
                id = null,
                isRemovable = true,
                name = listView.name + " Copy"
            )

        val newListId = listRepo.insertList(listModel).toInt()

        val listWords = listWordsRepo.getListWordsByListId(listView.id?:0)
        val newListContents = listWords.map {
            it.copy(listId = newListId)
        }
        listWordsRepo.insertListWord(newListContents)
    }
}
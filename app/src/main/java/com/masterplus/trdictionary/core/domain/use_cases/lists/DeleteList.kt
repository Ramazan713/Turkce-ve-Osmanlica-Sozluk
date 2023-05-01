package com.masterplus.trdictionary.core.domain.use_cases.lists

import com.masterplus.trdictionary.core.data.local.TransactionProvider
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.data.local.mapper.toListModel
import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.domain.repo.ListWordsRepo
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.domain.repo.SavePointRepo
import javax.inject.Inject

class DeleteList @Inject constructor(
    private val listRepo: ListRepo,
    private val listWordsRepo: ListWordsRepo,
    private val savePointRepo: SavePointRepo,
    private val transactionProvider: TransactionProvider
){

    suspend fun invoke(listView: ListView){
        transactionProvider.runAsTransaction {
            listWordsRepo.deleteListWordsByListId(listView.id?:0)
            listRepo.deleteList(listView.toListModel())

            val saveKey = SavePointDestination.List(listView.id?:0).toSaveKey()
            savePointRepo.deleteSavePointsBySaveKey(saveKey)
        }
    }
}
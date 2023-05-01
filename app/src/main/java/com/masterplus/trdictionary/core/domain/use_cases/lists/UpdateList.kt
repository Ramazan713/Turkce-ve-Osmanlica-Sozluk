package com.masterplus.trdictionary.core.domain.use_cases.lists

import com.masterplus.trdictionary.core.data.local.mapper.toListModel
import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import javax.inject.Inject

class UpdateList @Inject constructor(
    private val listRepo: ListRepo
) {

    suspend fun invoke(listView: ListView, newName: String? = null,
                       newIsArchive: Boolean? = null){
        val listModel = listView.toListModel().let {
            it.copy(name = newName ?: it.name, isArchive = newIsArchive ?: it.isArchive)
        }
        listRepo.updateList(listModel)
    }

}
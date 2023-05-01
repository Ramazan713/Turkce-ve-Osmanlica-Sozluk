package com.masterplus.trdictionary.core.domain.use_cases.lists

import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.domain.repo.ListViewRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLists @Inject constructor(
    private val listViewRepo: ListViewRepo
){
    fun invoke(isArchive: Boolean): Flow<List<ListView>> {
        return listViewRepo.getListViews(isArchive)
    }
}
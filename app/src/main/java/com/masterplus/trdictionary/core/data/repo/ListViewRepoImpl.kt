package com.masterplus.trdictionary.core.data.repo

import com.masterplus.trdictionary.core.data.local.mapper.toListView
import com.masterplus.trdictionary.core.data.local.services.ListViewDao
import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.domain.repo.ListViewRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ListViewRepoImpl @Inject constructor(
    private val listViewDao: ListViewDao
): ListViewRepo {
    override fun getListViews(isArchive: Boolean): Flow<List<ListView>> {
        return listViewDao.getListViews(isArchive)
            .map { items-> items.map { it.toListView() } }
    }

    override fun getRemovableListViews(isArchive: Boolean?): Flow<List<ListView>> {
        val listViews = if(isArchive!= null) listViewDao.getRemovableListViews(isArchive)
            else listViewDao.getRemovableAllListViews()

        return listViews.map { items-> items.map { it.toListView() } }
    }

    override fun getListViewsByWordId(wordId: Int): Flow<List<ListView>> {
        return listViewDao.getListViewsByWordId(wordId)
            .map { items->items.map { it.toListView() } }
    }

    override suspend fun getFavoriteList(): ListView? {
        return listViewDao.getFavoriteList()?.toListView()
    }
}
package com.masterplus.trdictionary.core.data.repo

import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.domain.repo.ListViewRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.*

class ListViewRepoFake: ListViewRepo{

    var listViews = listOf<ListView>()

    var wordIdListViewsMap = mapOf<Int,List<ListView>>()

    override fun getListViews(isArchive: Boolean): Flow<List<ListView>> {
        return flow {
            emit(listViews.filter { it.isArchive })
        }
    }

    override fun getRemovableListViews(isArchive: Boolean?): Flow<List<ListView>> {
        return flow {
            if(isArchive!=null){
                emit(listViews.filter { it.isArchive == isArchive && it.isRemovable })
            }else{
                emit(listViews.filter { it.isRemovable })
            }
        }
    }

    override fun getListViewsByWordId(wordId: Int): Flow<List<ListView>> {
        return flow {
            emit(wordIdListViewsMap[wordId] ?: emptyList())
        }
    }

    override suspend fun getFavoriteList(): ListView? {
        return listViews.firstOrNull { !it.isRemovable }
    }

}
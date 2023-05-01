package com.masterplus.trdictionary.core.domain.repo

import com.masterplus.trdictionary.core.domain.model.ListView
import kotlinx.coroutines.flow.Flow

interface ListViewRepo {

    fun getListViews(isArchive: Boolean): Flow<List<ListView>>

    fun getRemovableListViews(isArchive: Boolean? = null): Flow<List<ListView>>

    fun getListViewsByWordId(wordId: Int): Flow<List<ListView>>

    suspend fun getFavoriteList(): ListView?

}
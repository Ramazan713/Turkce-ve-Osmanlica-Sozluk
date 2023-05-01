package com.masterplus.trdictionary.core.domain.repo

import com.masterplus.trdictionary.core.domain.model.ListModel

interface ListRepo {

    suspend fun insertList(listModel: ListModel): Long

    suspend fun updateList(listModel: ListModel)

    suspend fun deleteList(listModel: ListModel)

    suspend fun getMaxPos(): Int

    suspend fun getListById(listId: Int): ListModel?

    suspend fun isFavoriteList(listId: Int): Boolean
}
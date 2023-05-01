package com.masterplus.trdictionary.core.data.repo

import com.masterplus.trdictionary.core.data.local.mapper.toListEntity
import com.masterplus.trdictionary.core.data.local.mapper.toListModel
import com.masterplus.trdictionary.core.data.local.services.ListDao
import com.masterplus.trdictionary.core.domain.model.ListModel
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import javax.inject.Inject

class ListRepoImpl @Inject constructor(
    private val listDao: ListDao
): ListRepo {
    override suspend fun insertList(listModel: ListModel): Long {
        return listDao.insertList(listModel.toListEntity())
    }

    override suspend fun updateList(listModel: ListModel) {
        listDao.updateList(listModel.toListEntity())
    }

    override suspend fun deleteList(listModel: ListModel) {
        listDao.deleteList(listModel.toListEntity())
    }

    override suspend fun getMaxPos(): Int {
        return listDao.getMaxPos()
    }

    override suspend fun getListById(listId: Int): ListModel? {
        return listDao.getListById(listId)?.toListModel()
    }

    override suspend fun isFavoriteList(listId: Int): Boolean {
        return listDao.isFavoriteList(listId)
    }
}
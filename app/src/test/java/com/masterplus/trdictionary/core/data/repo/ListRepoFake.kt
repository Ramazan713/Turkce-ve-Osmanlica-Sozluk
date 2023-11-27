package com.masterplus.trdictionary.core.data.repo

import androidx.compose.runtime.mutableStateListOf
import com.masterplus.trdictionary.core.domain.model.ListModel
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ListRepoFake: ListRepo {


    var favoriteListIds = listOf<Int>()
    private val _items = mutableListOf<ListModel>()
    val items: List<ListModel> get() = _items


    override suspend fun insertList(listModel: ListModel): Long {
        val lastId = _items.lastOrNull()?.id ?: 0
        _items.add(listModel.copy(id = lastId + 1))
        return (lastId + 1).toLong()
    }

    override suspend fun updateList(listModel: ListModel) {
        val index = _items.indexOfFirst { it.id == listModel.id }
        if(index != -1){
            _items[index] = listModel
        }
    }

    override suspend fun deleteList(listModel: ListModel) {
        _items.removeIf { it.id == listModel.id }
    }

    override suspend fun getMaxPos(): Int {
        return _items.maxOfOrNull { it.pos } ?: 0
    }

    override suspend fun getListById(listId: Int): ListModel? {
        return _items.firstOrNull { it.id == listId }
    }

    override suspend fun isFavoriteList(listId: Int): Boolean {
        return _items.firstOrNull { it.id == listId  }?.let {
            favoriteListIds.contains(it.id ?: return false)
        } ?: false
    }

}
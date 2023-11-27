package com.masterplus.trdictionary.core.data.repo

import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.domain.repo.SavePointRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.junit.jupiter.api.Assertions.*

class SavePointRepoFake: SavePointRepo{

    private val _items = MutableStateFlow<List<SavePoint>>(listOf())

    override fun getSavePointsFlow(typeIds: List<Int>): Flow<List<SavePoint>> {
        return _items.map {items->
            items.filter { typeIds.contains(it.savePointDestination.type.typeId) }
        }
    }

    override fun getSavePointsFlowBySaveKey(saveKey: String): Flow<List<SavePoint>> {
        return _items.map {items->
            items.filter { it.savePointDestination.toSaveKey() == saveKey }
        }
    }

    override suspend fun insertSavePoint(savePoint: SavePoint) {
        _items.update { it + savePoint }
    }

    override suspend fun deleteSavePoint(savePoint: SavePoint) {
        _items.update { it - savePoint }
    }

    override suspend fun getSavePointById(id: Int): SavePoint? {
        return _items.value.firstOrNull { it.id == id }
    }

    override suspend fun updateSavePoint(savePoint: SavePoint) {
        _items.update { items->
            val index = items.indexOfFirst { it.id == savePoint.id }
            if(index != -1 ){
                items.toMutableList().apply {
                    set(index,savePoint)
                }
            }else{
                items
            }
        }
    }

    override suspend fun deleteSavePointsBySaveKey(saveKey: String) {
        _items.update { items->
            items.filter { it.savePointDestination.toSaveKey() != saveKey }
        }
    }

}
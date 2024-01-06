package com.masterplus.trdictionary.features.search.data

import com.masterplus.trdictionary.core.utils.sample_data.history
import com.masterplus.trdictionary.features.search.domain.model.History
import com.masterplus.trdictionary.features.search.domain.repo.HistoryRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.junit.jupiter.api.Assertions.*
import java.util.Calendar
import java.util.UUID
import kotlin.random.Random

class HistoryRepoFake: HistoryRepo {


    private val _state = MutableStateFlow(listOf<History>(
        history(),
        history(id = 2)
    ))


    fun setInitData(histories: List<History>){
        _state.tryEmit(histories)
    }

    override fun getFlowHistories(): Flow<List<History>> {
        return _state.asStateFlow()
    }

    override suspend fun insertOrUpdateHistory(query: String) {
        _state.update {histories->
            val mutableHistories = histories.toMutableList()
            val index = histories.indexOfFirst { it.content == query }
            if(index == -1){
                val history = history(content = query, id = Random.nextInt(0, Int.MAX_VALUE))
                mutableHistories.add(history)
            }else{
                mutableHistories[index] = histories[index].copy(content = query)
            }
            mutableHistories
        }
    }

    override suspend fun deleteHistories() {
        _state.update {
            listOf()
        }
    }

    override suspend fun deleteHistory(history: History) {
        _state.update {histories->
            histories.filter { it != history }
        }
    }

}
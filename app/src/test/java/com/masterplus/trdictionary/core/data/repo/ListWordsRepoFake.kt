package com.masterplus.trdictionary.core.data.repo

import com.masterplus.trdictionary.core.domain.model.ListWords
import com.masterplus.trdictionary.core.domain.repo.ListWordsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class ListWordsRepoFake: ListWordsRepo{


    var removableWordIds = listOf<Int>()
    var favoriteWordIds = listOf<Int>()

    private val _items = mutableListOf<ListWords>()

    override suspend fun insertListWord(listWords: ListWords) {
        _items.add(listWords)
    }

    override suspend fun insertListWord(listWords: List<ListWords>) {
        _items.addAll(listWords)
    }

    override suspend fun updateListWords(listWords: ListWords) {
        _items.indexOfFirst { it.wordId == listWords.wordId && it.listId == listWords.listId }.let { index->
            if(index != -1){
                _items[index] = listWords
            }
        }
    }

    override suspend fun deleteListWord(listWords: ListWords) {
        _items.remove(listWords)
    }

    override suspend fun getListWord(wordId: Int, listId: Int): ListWords? {
        return _items.firstOrNull { it.listId == listId && it.wordId == wordId }
    }

    override suspend fun getListWordsByListId(listId: Int): List<ListWords> {
        return _items.filter { it.listId == listId }
    }

    override fun hasWordInFavoriteListFlow(wordId: Int): Flow<Boolean> {
        return flow {
            emit(favoriteWordIds.contains(wordId))
        }
    }

    override fun hasWordInRemovableListFlow(wordId: Int): Flow<Boolean> {
        return flow {
            emit(removableWordIds.contains(wordId))
        }
    }

    override suspend fun hasWordInFavoriteList(wordId: Int): Boolean {
        return favoriteWordIds.contains(wordId)
    }

    override suspend fun hasWordInRemovableList(wordId: Int): Boolean {
        return removableWordIds.contains(wordId)
    }

    override suspend fun deleteListWordsByListId(listId: Int) {
        _items.removeIf { it.listId == listId }
    }

}
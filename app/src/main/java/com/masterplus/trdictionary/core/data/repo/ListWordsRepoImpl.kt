package com.masterplus.trdictionary.core.data.repo

import com.masterplus.trdictionary.core.data.local.mapper.toListWords
import com.masterplus.trdictionary.core.data.local.mapper.toListWordsEntity
import com.masterplus.trdictionary.core.data.local.services.ListWordsDao
import com.masterplus.trdictionary.core.domain.model.ListWords
import com.masterplus.trdictionary.core.domain.repo.ListWordsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListWordsRepoImpl @Inject constructor(
    private val listWordsDao: ListWordsDao
): ListWordsRepo {
    override suspend fun insertListWord(listWords: ListWords) {
        listWordsDao.insertListWord(listWords.toListWordsEntity())
    }

    override suspend fun updateListWords(listWords: ListWords) {
        listWordsDao.updateListWord(listWords.toListWordsEntity())
    }

    override suspend fun deleteListWord(listWords: ListWords) {
        listWordsDao.deleteListWord(listWords.toListWordsEntity())
    }

    override suspend fun getListWord(wordId: Int, listId: Int): ListWords? {
        return listWordsDao.getListWordsEntity(wordId, listId)?.toListWords()
    }

    override suspend fun insertListWord(listWords: List<ListWords>) {
        listWordsDao.insertListWords(listWords.map { it.toListWordsEntity() })
    }

    override suspend fun getListWordsByListId(listId: Int): List<ListWords> {
        return listWordsDao.getListWordsByListId(listId).map {
            it.toListWords()
        }
    }

    override fun hasWordInFavoriteListFlow(wordId: Int): Flow<Boolean> {
        return listWordsDao.hasWordInFavoriteListFlow(wordId)
    }

    override fun hasWordInRemovableListFlow(wordId: Int): Flow<Boolean> {
        return listWordsDao.hasWordInRemovableListFlow(wordId)
    }

    override suspend fun hasWordInFavoriteList(wordId: Int): Boolean {
        return listWordsDao.hasWordInFavoriteList(wordId)
    }

    override suspend fun hasWordInRemovableList(wordId: Int): Boolean {
        return listWordsDao.hasWordInRemovableList(wordId)
    }

    override suspend fun deleteListWordsByListId(listId: Int) {
        listWordsDao.deleteListWordsByListId(listId)
    }
}
package com.masterplus.trdictionary.core.domain.repo

import com.masterplus.trdictionary.core.domain.model.ListWords
import kotlinx.coroutines.flow.Flow

interface ListWordsRepo {

    suspend fun insertListWord(listWords: ListWords)

    suspend fun updateListWords(listWords: ListWords)

    suspend fun deleteListWord(listWords: ListWords)

    suspend fun getListWord(wordId: Int, listId: Int): ListWords?

    suspend fun insertListWord(listWords: List<ListWords>)

    suspend fun getListWordsByListId(listId: Int): List<ListWords>

    fun hasWordInFavoriteListFlow(wordId: Int): Flow<Boolean>

    fun hasWordInRemovableListFlow(wordId: Int): Flow<Boolean>

    suspend fun hasWordInFavoriteList(wordId: Int): Boolean

    suspend fun hasWordInRemovableList(wordId: Int): Boolean


    suspend fun deleteListWordsByListId(listId: Int)

}
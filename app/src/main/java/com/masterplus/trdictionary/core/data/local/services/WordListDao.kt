package com.masterplus.trdictionary.core.data.local.services

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.trdictionary.core.data.local.entities.relations.SimpleWordResultRelation

@Dao
interface WordListDao {

    @Transaction
    @Query("""
        select * from words where dictTypeId = :dictTypeId and showInQuery = 1 order by lower(searchWord)
    """)
    fun getWordsWithDictType(dictTypeId: Int): PagingSource<Int, SimpleWordResultRelation>

    @Transaction
    @Query("""
        select * from words where dictTypeId = :dictTypeId and showInQuery = 1 and word Like :c||'%' order by lower(word)
    """)
    fun getAlphabeticWordsWithDictType(dictTypeId: Int, c: String): PagingSource<Int, SimpleWordResultRelation>

    @Transaction
    @Query("""
        select * from words where dictTypeId = :dictTypeId and showInQuery = 1 order by randomOrder
    """)
    fun getWordsWithDictTypeRandomOrder(dictTypeId: Int): PagingSource<Int, SimpleWordResultRelation>


    @Transaction
    @Query("""
        select * from words where showInQuery = 1 order by lower(searchWord)
    """)
    fun getAllWords(): PagingSource<Int, SimpleWordResultRelation>

    @Transaction
    @Query("""
        select * from words where showInQuery = 1 and word Like :c||'%' order by lower(word)
    """)
    fun getAllAlphabeticWords(c: String): PagingSource<Int, SimpleWordResultRelation>

    @Transaction
    @Query("""
        select * from words where showInQuery = 1 order by randomOrder
    """)
    fun getAllWordsRandomOrder(): PagingSource<Int, SimpleWordResultRelation>


    @Transaction
    @Query("""
        select * from words where wordTypeId = :wordTypeId and showInQuery = 1 order by lower(searchWord)
    """)
    fun getAllProverbIdiomWords(wordTypeId: Int): PagingSource<Int, SimpleWordResultRelation>

    @Transaction
    @Query("""
        select * from words where wordTypeId = :wordTypeId 
        and showInQuery = 1 and word Like :c||'%' order by lower(word)
    """)
    fun getAllProverbIdiomAlphabeticWords(wordTypeId: Int, c: String): PagingSource<Int, SimpleWordResultRelation>

    @Transaction
    @Query("""
        select * from words where showInQuery = 1 and
        wordTypeId = :wordTypeId order by randomOrder
    """)
    fun getAllProverbIdiomWordsRandomOrder(wordTypeId: Int): PagingSource<Int, SimpleWordResultRelation>



    @Transaction
    @Query("""
        select W.* from words W, listWords LW where W.id = LW.wordId and W.showInQuery = 1 and
        LW.listId = :listId order by pos desc
    """)
    fun getWordsByListId(listId: Int): PagingSource<Int, SimpleWordResultRelation>



    @Query("""
        select count(*) from words where showInQuery = 1 and randomOrder < :randomOrder order by randomOrder 
    """)
    suspend fun getAllDictPosByRandomOrder(randomOrder: Int): Int

}
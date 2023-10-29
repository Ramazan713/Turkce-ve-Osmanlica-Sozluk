package com.masterplus.trdictionary.core.data.local.services

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.trdictionary.core.data.local.entities.relations.SimpleWordResultRelation

@Dao
interface ShortInfoDao {

    @Query("""
        select count(*) from words
    """)
    suspend fun countWords(): Int

    @Transaction
    @Query("""
        select * from words limit 1 offset :offset
    """)
    suspend fun getWord(offset: Int): SimpleWordResultRelation?


    @Query("""
        select count(*) from words where wordTypeId = :wordTypeId
    """)
    suspend fun countWordsByTypeId(wordTypeId: Int): Int

    @Transaction
    @Query("""
        select * from words where wordTypeId = :wordTypeId limit 1 offset :offset   
    """)
    suspend fun getWordByTypeId(wordTypeId: Int, offset: Int): SimpleWordResultRelation?


}
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


    @Transaction
    @Query("""
        select count(*) from words W, ProverbIdiomWords PW where W.id = PW.wordId and
        PW.type = :typeId
    """)
    suspend fun countWordsByTypeId(typeId: Int): Int

    @Transaction
    @Query("""
        select W.* from words W, ProverbIdiomWords PW where W.id = PW.wordId and
        PW.type = :typeId limit 1 offset :offset   
    """)
    suspend fun getWordByTypeId(typeId: Int, offset: Int): SimpleWordResultRelation?


}
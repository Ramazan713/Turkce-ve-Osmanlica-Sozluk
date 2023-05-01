package com.masterplus.trdictionary.core.data.local.services

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.trdictionary.core.data.local.entities.relations.SimpleWordResultRelation

@Dao
interface SearchDao {

    @Transaction
    @Query("""
        select w.* from wordsFts ctfs, Words w
        where w.id = ctfs.rowid and w.showInQuery = 1 and ctfs.wordsFts match :ftsSearch
        order by case when lower(ctfs.word) = :queryRaw then 1 
        when lower(ctfs.searchWord) = :queryRaw then 2 when ctfs.word like :queryOrderForLike then 3 
        when ctfs.searchWord like :queryOrderForLike then 4 else 5 end
        limit :limit
    """)
    suspend fun searchWords(
        ftsSearch: String,
        queryOrderForLike: String,
        queryRaw: String,
        limit: Int
    ): List<SimpleWordResultRelation>


    @Transaction
    @Query("""
        select w.* from wordsFts ctfs, Words w
        where w.id = ctfs.rowid and w.showInQuery = 1 and w.dictType = :dictType and ctfs.wordsFts match :ftsSearch
        order by case when lower(ctfs.word) = :queryRaw then 1 
        when lower(ctfs.searchWord) = :queryRaw then 2 when ctfs.word like :queryOrderForLike then 3 
        when ctfs.searchWord like :queryOrderForLike then 4 else 5 end
        limit :limit
    """)
    suspend fun searchWordsWithDictType(
        ftsSearch: String,
        queryOrderForLike: String,
        queryRaw: String,
        dictType: Int,
        limit: Int
    ): List<SimpleWordResultRelation>


    @Transaction
    @Query("""
        select w.* from wordsFts ctfs, Words w, ProverbIdiomWords pw
        where w.id = ctfs.rowid and pw.wordId = w.id and w.showInQuery = 1 and pw.type = :typeId and ctfs.wordsFts match :ftsSearch
        order by case when lower(ctfs.word) = :queryRaw then 1 
        when lower(ctfs.searchWord) = :queryRaw then 2 when ctfs.word like :queryOrderForLike then 3 
        when ctfs.searchWord like :queryOrderForLike then 4 else 5 end
        limit :limit
    """)
    suspend fun searchWordsWithTypeId(
        ftsSearch: String,
        queryOrderForLike: String,
        queryRaw: String,
        typeId: Int,
        limit: Int
    ): List<SimpleWordResultRelation>

    @Transaction
    @Query("""
        select W.* from words W, Meanings M 
        where W.id = M.wordId and W.showInQuery = 1 and M.meaning like :querySearchFull group by M.wordId
        order by case when lower(M.meaning) = :queryRaw then 1 when lower(M.meaning) = :queryOrderForLike then 2
        else 3 end limit :limit
    """)
    suspend fun searchWordsFromMeaning(
        querySearchFull: String,
        queryOrderForLike: String,
        queryRaw: String,
        limit: Int
    ): List<SimpleWordResultRelation>

    @Transaction
    @Query("""
        select W.* from words W, Meanings M 
        where W.id = M.wordId and W.showInQuery = 1 and W.dictType = :dictType and M.meaning like :querySearchFull group by M.wordId
        order by case when lower(M.meaning) = :queryRaw then 1 when lower(M.meaning) = :queryOrderForLike then 2
        else 3 end limit :limit
    """)
    suspend fun searchWordsFromMeaningWithDictType(
        querySearchFull: String,
        queryOrderForLike: String,
        queryRaw: String,
        dictType: Int,
        limit: Int
    ): List<SimpleWordResultRelation>

    @Transaction
    @Query("""
        select W.* from words W, meanings M, proverbIdiomWords PW
        where W.id = M.wordId and PW.wordId = W.id and W.showInQuery = 1 and PW.type = :typeId and 
        M.meaning like :querySearchFull group by M.wordId
        order by case when lower(M.meaning) = :queryRaw then 1 when lower(M.meaning) = :queryOrderForLike then 2
        else 3 end limit :limit
    """)
    suspend fun searchWordsFromMeaningWithTypeId(
        querySearchFull: String,
        queryOrderForLike: String,
        queryRaw: String,
        typeId: Int,
        limit: Int
    ): List<SimpleWordResultRelation>



}
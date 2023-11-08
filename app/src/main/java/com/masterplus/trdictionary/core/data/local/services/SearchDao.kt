package com.masterplus.trdictionary.core.data.local.services

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.trdictionary.core.data.local.entities.relations.SimpleWordResultRelation
import com.masterplus.trdictionary.core.data.local.entities.relations.WordWithSimilarRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Transaction
    @Query("""
        select w.* from wordsFts ctfs, wordDetailsView w
        where w.id = ctfs.rowid and w.showInQuery = 1 and ctfs.wordsFts match :ftsSearch
        order by case when lower(ctfs.word) = :queryRaw then 1 
        when lower(ctfs.searchWord) = :queryRaw then 2 when ctfs.word like :queryOrderForLike then 3 
        when ctfs.searchWord like :queryOrderForLike then 4 else 5 end
        limit :limit
    """)
    fun searchWords(
        ftsSearch: String,
        queryOrderForLike: String,
        queryRaw: String,
        limit: Int
    ): Flow<List<WordWithSimilarRelation>>


    @Transaction
    @Query("""
        select w.* from wordsFts ctfs, wordDetailsView w
        where w.id = ctfs.rowid and w.showInQuery = 1 and w.dictTypeId = :dictTypeId and ctfs.wordsFts match :ftsSearch
        order by case when lower(ctfs.word) = :queryRaw then 1 
        when lower(ctfs.searchWord) = :queryRaw then 2 when ctfs.word like :queryOrderForLike then 3 
        when ctfs.searchWord like :queryOrderForLike then 4 else 5 end
        limit :limit
    """)
    fun searchWordsWithDictType(
        ftsSearch: String,
        queryOrderForLike: String,
        queryRaw: String,
        dictTypeId: Int,
        limit: Int
    ): Flow<List<WordWithSimilarRelation>>


    @Transaction
    @Query("""
        select w.* from wordsFts ctfs, wordDetailsView w
        where w.id = ctfs.rowid and w.showInQuery = 1 and w.wordTypeId = :wordTypeId and ctfs.wordsFts match :ftsSearch
        order by case when lower(ctfs.word) = :queryRaw then 1 
        when lower(ctfs.searchWord) = :queryRaw then 2 when ctfs.word like :queryOrderForLike then 3 
        when ctfs.searchWord like :queryOrderForLike then 4 else 5 end
        limit :limit
    """)
    fun searchWordsWithTypeId(
        ftsSearch: String,
        queryOrderForLike: String,
        queryRaw: String,
        wordTypeId: Int,
        limit: Int
    ): Flow<List<WordWithSimilarRelation>>

    @Transaction
    @Query("""
        select W.* from wordDetailsView W, Meanings M 
        where W.id = M.wordId and W.showInQuery = 1 and M.meaning like :querySearchFull group by M.wordId
        order by case when lower(M.meaning) = :queryRaw then 1 when lower(M.meaning) = :queryOrderForLike then 2
        else 3 end limit :limit
    """)
    fun searchWordsFromMeaning(
        querySearchFull: String,
        queryOrderForLike: String,
        queryRaw: String,
        limit: Int
    ): Flow<List<WordWithSimilarRelation>>

    @Transaction
    @Query("""
        select W.* from wordDetailsView W, Meanings M 
        where W.id = M.wordId and W.showInQuery = 1 and W.dictTypeId = :dictTypeId and M.meaning like :querySearchFull group by M.wordId
        order by case when lower(M.meaning) = :queryRaw then 1 when lower(M.meaning) = :queryOrderForLike then 2
        else 3 end limit :limit
    """)
    fun searchWordsFromMeaningWithDictType(
        querySearchFull: String,
        queryOrderForLike: String,
        queryRaw: String,
        dictTypeId: Int,
        limit: Int
    ): Flow<List<WordWithSimilarRelation>>

    @Transaction
    @Query("""
        select W.* from wordDetailsView W, meanings M
        where W.id = M.wordId and W.showInQuery = 1 and W.wordTypeId = :wordTypeId and 
        M.meaning like :querySearchFull group by M.wordId
        order by case when lower(M.meaning) = :queryRaw then 1 when lower(M.meaning) = :queryOrderForLike then 2
        else 3 end limit :limit
    """)
    fun searchWordsFromMeaningWithTypeId(
        querySearchFull: String,
        queryOrderForLike: String,
        queryRaw: String,
        wordTypeId: Int,
        limit: Int
    ): Flow<List<WordWithSimilarRelation>>



}
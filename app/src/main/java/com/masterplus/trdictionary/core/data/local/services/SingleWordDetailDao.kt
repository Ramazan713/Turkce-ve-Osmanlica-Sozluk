package com.masterplus.trdictionary.core.data.local.services

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.masterplus.trdictionary.core.data.local.entities.relations.SimpleWordResultRelation
import com.masterplus.trdictionary.core.data.local.entities.relations.WordMeaningsRelation
import com.masterplus.trdictionary.core.data.local.entities.relations.WordWithSimilarRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface SingleWordDetailDao {

    @Transaction
    @Query("""
        select * from wordDetailsView where id=:wordId 
    """)
    fun getWordWithSimilarFlow(wordId: Int): Flow<WordWithSimilarRelation?>

    @Transaction
    @Query("select * from wordDetailsView where id=:id")
    suspend fun getWordMeaningsByWordId(id: Int): WordMeaningsRelation?

    @Transaction
    @Query("""
        select * from wordDetailsView where lower(word) = lower(:word) and dictTypeId = :dictTypeId
    """)
    suspend fun getWordExamplesByWordCount(word: String, dictTypeId: Int): WordMeaningsRelation?


    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("""
        select W.* from compoundWords CW, wordDetailsView W where W.id = CW.compoundWordId and CW.wordId = :wordId
    """)
    suspend fun getCompoundSimpleWordsByWordId(wordId: Int): List<SimpleWordResultRelation>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("""
        select W.* from wordDetailsView W, proverbIdiomWords PW where W.id = PW.wordId and wordTypeId != 1 and wordId != :wordId 
        and PW.groupId = (select groupId from proverbIdiomWords where wordId = :wordId)
    """)
    suspend fun getProverbIdiomWordsByWordId(wordId: Int): List<SimpleWordResultRelation>
}
package com.masterplus.trdictionary.core.data.local.services

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.trdictionary.core.data.local.entities.relations.SimpleWordResultRelation
import com.masterplus.trdictionary.core.data.local.entities.relations.WordMeaningsRelation
import com.masterplus.trdictionary.core.data.local.entities.relations.WordListInfoSimilaritiesRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface SingleWordDetailDao {

    @Transaction
    @Query("""
        select * from words where id=:wordId 
    """)
    fun getWordWithSimilarFlow(wordId: Int): Flow<WordListInfoSimilaritiesRelation?>

    @Transaction
    @Query("select * from words where id=:id")
    suspend fun getWordMeaningsByWordId(id: Int): WordMeaningsRelation?

    @Transaction
    @Query("""
        select * from words where lower(word) = lower(:word) and dictType = :dictType
    """)
    suspend fun getWordExamplesByWordCount(word: String, dictType: Int): WordMeaningsRelation?


    @Transaction
    @Query("""
        select W.* from compoundWords CW, words W where W.id = CW.compoundWordId and CW.wordId = :wordId
    """)
    suspend fun getCompoundSimpleWordsByWordId(wordId: Int): List<SimpleWordResultRelation>

    @Transaction
    @Query("""
        select W.* from words W, proverbIdiomWords PW where W.id = PW.wordId and type != 1 and wordId != :wordId 
        and PW.groupId = (select groupId from proverbIdiomWords where wordId = :wordId)
    """)
    suspend fun getProverbIdiomWordsByWordId(wordId: Int): List<SimpleWordResultRelation>
}
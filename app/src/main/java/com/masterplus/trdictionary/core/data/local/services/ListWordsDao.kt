package com.masterplus.trdictionary.core.data.local.services

import androidx.room.*
import com.masterplus.trdictionary.core.data.local.entities.ListWordsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListWordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListWord(listWord: ListWordsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListWords(listWords: List<ListWordsEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateListWord(listWord: ListWordsEntity)

    @Delete
    suspend fun deleteListWord(listWords: ListWordsEntity)

    @Query("""delete from listWords where listId = :listId""")
    suspend fun deleteListWordsByListId(listId: Int)

    @Query("""select * from listWords where 
        wordId=:wordId and listId=:listId""")
    suspend fun getListWordsEntity(wordId: Int, listId: Int): ListWordsEntity?


    @Query("""
        select * from listWords where listId=:listId
    """)
    suspend fun getListWordsByListId(listId: Int): List<ListWordsEntity>

    @Transaction
    @Query("""
        select exists(select * from listWords LW, lists L
        where LW.listId = L.id and L.isRemovable = 0 and LW.wordId = :wordId)
    """)
    fun hasWordInFavoriteListFlow(wordId: Int): Flow<Boolean>

    @Transaction
    @Query("""
        select exists(select * from listWords LW, lists L
        where LW.listId = L.id and L.isRemovable = 1 and LW.wordId = :wordId)
    """)
    fun hasWordInRemovableListFlow(wordId: Int): Flow<Boolean>


    @Transaction
    @Query("""
        select exists(select * from listWords LW, lists L
        where LW.listId = L.id and L.isRemovable = 0 and LW.wordId = :wordId)
    """)
    suspend fun hasWordInFavoriteList(wordId: Int): Boolean

    @Transaction
    @Query("""
        select exists(select * from listWords LW, lists L
        where LW.listId = L.id and L.isRemovable = 1 and LW.wordId = :wordId)
    """)
    suspend fun hasWordInRemovableList(wordId: Int): Boolean

}
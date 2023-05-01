package com.masterplus.trdictionary.core.data.local.services

import androidx.room.*
import com.masterplus.trdictionary.core.data.local.entities.HistoryEntity
import com.masterplus.trdictionary.core.data.local.entities.ListEntity
import com.masterplus.trdictionary.core.data.local.entities.ListWordsEntity
import com.masterplus.trdictionary.core.data.local.entities.SavePointEntity

@Dao
interface LocalBackupDao {

    @Query("""select * from histories""")
    suspend fun getHistories(): List<HistoryEntity>

    @Query("""select * from lists""")
    suspend fun getLists(): List<ListEntity>

    @Query("""select * from listWords""")
    suspend fun getListWords(): List<ListWordsEntity>

    @Query("""select * from savePoints""")
    suspend fun getSavePoints(): List<SavePointEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistories(histories: List<HistoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLists(lists: List<ListEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListWords(listWords: List<ListWordsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavePoints(savePoints: List<SavePointEntity>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ListEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListWord(listWord: ListWordsEntity): Long


    @Query("""delete from savePoints""")
    suspend fun deleteSavePointsWithQuery()

    @Query("""delete from lists""")
    suspend fun deleteListsWithQuery()

    @Query("""delete from listWords""")
    suspend fun deleteListWordsWithQuery()

    @Query("""delete from histories""")
    suspend fun deleteHistoriesWithQuery()


    @Query("""select ifnull(max(pos),0) from lists""")
    suspend fun getListMaxPos(): Int


    @Transaction
    suspend fun deleteUserData(){
        deleteListWordsWithQuery()
        deleteSavePointsWithQuery()
        deleteListsWithQuery()
        deleteHistoriesWithQuery()
    }

}
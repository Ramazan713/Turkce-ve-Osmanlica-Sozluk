package com.masterplus.trdictionary.core.data.local.services

import androidx.room.*
import com.masterplus.trdictionary.core.data.local.entities.ListEntity

@Dao
interface ListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(listEntity: ListEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateList(listEntity: ListEntity)

    @Delete
    suspend fun deleteList(listEntity: ListEntity)

    @Query("""select ifnull(max(pos),0) from lists""")
    suspend fun getMaxPos(): Int

    @Query("""select * from lists where id = :listId""")
    suspend fun getListById(listId: Int): ListEntity?


    @Query("""
        select ifnull((select  not isRemovable from lists where id = :listId), 0)
    """)
    suspend fun isFavoriteList(listId: Int): Boolean

}
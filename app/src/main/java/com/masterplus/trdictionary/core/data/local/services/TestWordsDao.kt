package com.masterplus.trdictionary.core.data.local.services

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.masterplus.trdictionary.core.data.local.entities.WordEntity

@Dao
interface TestWordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<WordEntity>)

}
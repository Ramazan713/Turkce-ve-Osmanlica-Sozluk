package com.masterplus.trdictionary.features.home.domain.manager

import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoCollectionResult
import kotlinx.coroutines.flow.Flow

interface ShortInfoManager {

    suspend fun getWord(shortInfoEnum: ShortInfoEnum, refresh: Boolean): SimpleWordResult?

    suspend fun getWords(refresh: Boolean): ShortInfoCollectionResult

    fun getWordsFlow(): Flow<ShortInfoCollectionResult>

    suspend fun refreshWords()

    suspend fun refreshWord(shortInfo: ShortInfoEnum)

    suspend fun checkDayForRefresh(): Boolean
}
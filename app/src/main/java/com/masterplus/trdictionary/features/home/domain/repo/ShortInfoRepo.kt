package com.masterplus.trdictionary.features.home.domain.repo

import com.masterplus.trdictionary.core.domain.enums.ProverbIdiomEnum
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult

interface ShortInfoRepo {

    suspend fun countWords(): Int

    suspend fun getWord(offset: Int): SimpleWordResult?

    suspend fun countWordsByTypeId(proverbIdiomEnum: ProverbIdiomEnum): Int

    suspend fun getWordByTypeId(proverbIdiomEnum: ProverbIdiomEnum, offset: Int): SimpleWordResult?
}
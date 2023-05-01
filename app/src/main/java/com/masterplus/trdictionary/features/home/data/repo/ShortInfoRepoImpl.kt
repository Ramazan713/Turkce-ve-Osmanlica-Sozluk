package com.masterplus.trdictionary.features.home.data.repo

import com.masterplus.trdictionary.core.data.local.mapper.toSimpleResult
import com.masterplus.trdictionary.core.domain.enums.ProverbIdiomEnum
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.data.local.services.ShortInfoDao
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoRepo
import javax.inject.Inject

class ShortInfoRepoImpl @Inject constructor(
    private val shortInfoDao: ShortInfoDao
): ShortInfoRepo {
    override suspend fun countWords(): Int {
        return shortInfoDao.countWords()
    }

    override suspend fun getWord(offset: Int): SimpleWordResult? {
        return shortInfoDao.getWord(offset)
            ?.toSimpleResult()
    }

    override suspend fun countWordsByTypeId(proverbIdiomEnum: ProverbIdiomEnum): Int {
        return shortInfoDao.countWordsByTypeId(proverbIdiomEnum.typeId)
    }

    override suspend fun getWordByTypeId(
        proverbIdiomEnum: ProverbIdiomEnum,
        offset: Int
    ): SimpleWordResult? {
        return shortInfoDao.getWordByTypeId(proverbIdiomEnum.typeId,offset)?.toSimpleResult()
    }

}
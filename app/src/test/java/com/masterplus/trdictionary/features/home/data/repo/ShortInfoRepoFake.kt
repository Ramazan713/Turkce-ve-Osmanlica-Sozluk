package com.masterplus.trdictionary.features.home.data.repo

import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.enums.ProverbIdiomEnum
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.utils.sample_data.simpleWordResult
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoRepo
import org.junit.Assert.*

class ShortInfoRepoFake: ShortInfoRepo{

    var words = listOf(simpleWordResult(), simpleWordResult(wordId = 2))

    override suspend fun countWords(): Int {
        return words.size
    }

    override suspend fun getWord(offset: Int): SimpleWordResult? {
        return words.getOrNull(offset)
    }

    override suspend fun countWordsByTypeId(proverbIdiomEnum: ProverbIdiomEnum): Int {
        return words.filter { it.word.wordType.id == proverbIdiomEnum.typeId }.size
    }

    override suspend fun getWordByTypeId(
        proverbIdiomEnum: ProverbIdiomEnum,
        offset: Int
    ): SimpleWordResult? {
        return words.filter {
            it.word.wordType.id == proverbIdiomEnum.typeId
        }.getOrNull(offset)
    }

}
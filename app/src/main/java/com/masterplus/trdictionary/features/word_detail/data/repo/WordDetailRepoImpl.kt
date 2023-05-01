package com.masterplus.trdictionary.features.word_detail.data.repo

import com.masterplus.trdictionary.core.data.local.mapper.toSimpleResult
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.features.word_detail.data.mapper.toWordMeanings
import com.masterplus.trdictionary.core.data.local.services.SingleWordDetailDao
import com.masterplus.trdictionary.features.word_detail.domain.model.WordMeanings
import com.masterplus.trdictionary.features.word_detail.domain.repo.WordDetailRepo
import com.masterplus.trdictionary.features.word_detail.data.mapper.toWordListInfoSimilarWords
import com.masterplus.trdictionary.features.word_detail.domain.model.WordListInfoSimilarWords
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WordDetailRepoImpl @Inject constructor(
    private val singleWordDetailDao: SingleWordDetailDao
): WordDetailRepo {

    override fun getWordWithSimilarFlow(wordId: Int): Flow<WordListInfoSimilarWords?> {
        return singleWordDetailDao.getWordWithSimilarFlow(wordId)
            .map { item->item?.toWordListInfoSimilarWords() }
    }

    override suspend fun getWordMeaningsWithWordId(wordId: Int): WordMeanings? {
        return singleWordDetailDao.getWordMeaningsByWordId(wordId)?.toWordMeanings()
    }

    override suspend fun getWordExamplesByWordCount(
        word: String,
        wordCount: Int,
        dictType: Int
    ): WordMeanings? {
        return singleWordDetailDao.getWordExamplesByWordCount(word, dictType)?.toWordMeanings()
    }

    override suspend fun getCompoundSimpleWordsByWordId(wordId: Int): List<SimpleWordResult> {
        return singleWordDetailDao.getCompoundSimpleWordsByWordId(wordId)
            .map { it.toSimpleResult() }
    }

    override suspend fun getProverbIdiomWordsBywordId(wordId: Int): List<SimpleWordResult> {
        return singleWordDetailDao.getProverbIdiomWordsByWordId(wordId)
            .map { it.toSimpleResult() }
    }


}
package com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo

import com.masterplus.trdictionary.core.data.local.mapper.toSimpleResult
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.data.local.mapper.toWordMeanings
import com.masterplus.trdictionary.core.data.local.services.SingleWordDetailDao
import com.masterplus.trdictionary.core.data.local.mapper.toWordWithSimilar
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilarRelationModel
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordDetailRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WordDetailRepoImpl @Inject constructor(
    private val singleWordDetailDao: SingleWordDetailDao
): WordDetailRepo {

    override fun getWordWithSimilarFlow(wordId: Int): Flow<WordWithSimilarRelationModel?> {
        return singleWordDetailDao.getWordWithSimilarFlow(wordId)
            .map { item->item?.toWordWithSimilar() }
    }

    override suspend fun getWordMeaningsWithWordId(wordId: Int): WordDetailMeanings? {
        return singleWordDetailDao.getWordMeaningsByWordId(wordId)?.toWordMeanings()
    }

    override suspend fun getWordExamplesByWordCount(
        word: String,
        wordCount: Int,
        dictType: Int
    ): WordDetailMeanings? {
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
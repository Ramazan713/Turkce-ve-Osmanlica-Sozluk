package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo

import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilarRelationModel
import kotlinx.coroutines.flow.Flow

interface WordDetailRepo {

    fun getWordWithSimilarFlow(wordId: Int): Flow<WordWithSimilarRelationModel?>

    suspend fun getWordMeaningsWithWordId(wordId: Int): WordDetailMeanings?

    suspend fun getCompoundSimpleWordsByWordId(wordId: Int): List<SimpleWordResult>

    suspend fun getProverbIdiomWordsBywordId(wordId: Int): List<SimpleWordResult>

}
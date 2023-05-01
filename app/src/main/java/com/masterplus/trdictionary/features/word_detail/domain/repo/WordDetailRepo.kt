package com.masterplus.trdictionary.features.word_detail.domain.repo

import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.features.word_detail.domain.model.WordMeanings
import com.masterplus.trdictionary.features.word_detail.domain.model.WordListInfoSimilarWords
import kotlinx.coroutines.flow.Flow

interface WordDetailRepo {

    fun getWordWithSimilarFlow(wordId: Int): Flow<WordListInfoSimilarWords?>

    suspend fun getWordMeaningsWithWordId(wordId: Int): WordMeanings?

    suspend fun getWordExamplesByWordCount(word: String, wordCount: Int, dictType: Int): WordMeanings?

    suspend fun getCompoundSimpleWordsByWordId(wordId: Int): List<SimpleWordResult>

    suspend fun getProverbIdiomWordsBywordId(wordId: Int): List<SimpleWordResult>

}
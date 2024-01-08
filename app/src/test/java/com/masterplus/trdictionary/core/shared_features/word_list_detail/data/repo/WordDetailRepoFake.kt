package com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo

import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilarRelationModel
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordDetailRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.*

class WordDetailRepoFake: WordDetailRepo {

    var wordWithSimilarRelationFlow = flow<WordWithSimilarRelationModel> {

    }

    var returnedWordMeaningsArr = listOf<WordDetailMeanings>()
    var returnedCompoundSimpleWords = listOf<SimpleWordResult>()
    var returnedProverbIdiomWords = listOf<SimpleWordResult>()


    override fun getWordWithSimilarFlow(wordId: Int): Flow<WordWithSimilarRelationModel?> {
        return wordWithSimilarRelationFlow.filter { it.wordDetailMeanings.wordId == wordId }
    }

    override suspend fun getWordMeaningsWithWordId(wordId: Int): WordDetailMeanings? {
        return returnedWordMeaningsArr.filter { it.wordId == wordId }.firstOrNull()
    }

    override suspend fun getCompoundSimpleWordsByWordId(wordId: Int): List<SimpleWordResult> {
        return returnedCompoundSimpleWords.filter { it.wordId == wordId }
    }

    override suspend fun getProverbIdiomWordsBywordId(wordId: Int): List<SimpleWordResult> {
        return returnedProverbIdiomWords.filter { it.wordId == wordId }
    }

}
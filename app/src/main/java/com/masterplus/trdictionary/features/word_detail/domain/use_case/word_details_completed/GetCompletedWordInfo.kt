package com.masterplus.trdictionary.features.word_detail.domain.use_case.word_details_completed

import com.masterplus.trdictionary.features.word_detail.domain.model.*
import com.masterplus.trdictionary.features.word_detail.domain.repo.WordDetailRepo
import javax.inject.Inject

class GetCompletedWordInfo @Inject constructor(
    private val wordDetailRepo: WordDetailRepo
){
    suspend operator fun invoke(word: WordWithSimilarRelationModel): WordWithSimilar {
        val similarWords = word.similarWords.mapNotNull {
            getWordDetailInfoFromWordId(it.id)
        }

        return WordWithSimilar(
            wordDetailMeanings = word.wordDetailMeanings,
            similarWords = similarWords
        )
    }
    private suspend fun getWordDetailInfoFromWordId(wordId: Int): WordDetailMeanings?{
        return wordDetailRepo.getWordMeaningsWithWordId(wordId)
    }
}
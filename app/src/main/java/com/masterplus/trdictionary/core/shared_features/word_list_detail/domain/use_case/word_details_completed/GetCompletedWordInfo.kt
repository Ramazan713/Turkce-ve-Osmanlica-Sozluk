package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed

import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilarRelationModel
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordDetailRepo
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
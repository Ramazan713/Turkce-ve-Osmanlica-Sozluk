package com.masterplus.trdictionary.features.word_detail.domain.use_case.word_details_completed

import com.masterplus.trdictionary.features.word_detail.domain.repo.WordDetailRepo
import com.masterplus.trdictionary.features.word_detail.domain.model.WordCompletedInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCompletedWordFlow @Inject constructor(
    private val wordDetailRepo: WordDetailRepo,
    private val getCompletedInfo: GetCompletedWordInfo
) {

    operator fun invoke(wordId: Int): Flow<WordCompletedInfo?>{

        return wordDetailRepo.getWordWithSimilarFlow(wordId).map {
            if(it == null) return@map null
            return@map getCompletedInfo(it)
        }
    }
}
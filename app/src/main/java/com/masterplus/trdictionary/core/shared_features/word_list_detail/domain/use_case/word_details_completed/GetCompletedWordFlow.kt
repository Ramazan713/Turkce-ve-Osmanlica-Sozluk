package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed

import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordDetailRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCompletedWordFlow @Inject constructor(
    private val wordDetailRepo: WordDetailRepo,
    private val getCompletedInfo: GetCompletedWordInfo
) {

    operator fun invoke(wordId: Int): Flow<WordWithSimilar?>{

        return wordDetailRepo.getWordWithSimilarFlow(wordId).map {
            if(it == null) return@map null
            return@map getCompletedInfo(it)
        }
    }
}
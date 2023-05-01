package com.masterplus.trdictionary.features.word_detail.domain.use_case.word_details_basic

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.features.word_detail.domain.repo.WordListRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListSimpleWords @Inject constructor(
    private val wordListRepo: WordListRepo
) {
    operator fun invoke(listId: Int): Flow<PagingData<SimpleWordResult>> {

        val config = PagingConfig(pageSize = K.wordListPageSize, enablePlaceholders = true,
            jumpThreshold = K.wordListJumpThreshold)

        return wordListRepo.getWordsByListId(listId,config)
    }
}
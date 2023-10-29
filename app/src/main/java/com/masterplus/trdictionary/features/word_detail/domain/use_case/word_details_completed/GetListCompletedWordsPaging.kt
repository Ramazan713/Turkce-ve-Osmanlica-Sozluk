package com.masterplus.trdictionary.features.word_detail.domain.use_case.word_details_completed

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.features.word_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.features.word_detail.domain.repo.WordListDetailRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetListCompletedWordsPaging @Inject constructor(
    private val wordListDetailRepo: WordListDetailRepo,
    private val getCompletedInfo: GetCompletedWordInfo
) {

    operator fun invoke(listId: Int) : Flow<PagingData<WordWithSimilar>> {
        val config = PagingConfig(pageSize = K.wordsDetailPagerPageSize, enablePlaceholders = true,
            jumpThreshold = K.wordsDetailPagerJumpThreshold)

        val wordCompletedInfoData = wordListDetailRepo.getWordsByListId(listId, config).map { pagingData->
            pagingData.map { word->
                getCompletedInfo(word)
            }
        }
        return wordCompletedInfoData
    }


}
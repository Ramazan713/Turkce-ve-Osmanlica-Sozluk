package com.masterplus.trdictionary.features.word_detail.domain.use_case.word_details_completed

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.features.word_detail.domain.repo.WordListDetailRepo
import com.masterplus.trdictionary.features.word_detail.domain.model.WordWithSimilar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoryCompletedWordsPaging @Inject constructor(
    private val wordListDetailRepo: WordListDetailRepo,
    private val getCompletedInfo: GetCompletedWordInfo
) {

    operator fun invoke(categoryEnum: CategoryEnum, subCategoryEnum: SubCategoryEnum, c: String?) : Flow<PagingData<WordWithSimilar>>{
        val config = PagingConfig(pageSize = K.wordsDetailPagerPageSize, enablePlaceholders = true,
            jumpThreshold = K.wordsDetailPagerJumpThreshold)

        val wordCompletedInfoData = wordListDetailRepo.getWords(categoryEnum,subCategoryEnum,c,config).map { pagingData->
            pagingData.map { word->
                getCompletedInfo(word)
            }
        }
        return wordCompletedInfoData
    }


}
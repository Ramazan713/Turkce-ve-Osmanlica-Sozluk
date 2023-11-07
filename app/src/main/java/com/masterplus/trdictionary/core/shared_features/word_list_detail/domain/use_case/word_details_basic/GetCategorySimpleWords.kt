package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_basic

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordListRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategorySimpleWords @Inject constructor(
    private val wordListRepo: WordListRepo
) {

    operator fun invoke(categoryEnum: CategoryEnum, subCategoryEnum: SubCategoryEnum, c: String?):
            Flow<PagingData<SimpleWordResult>> {

        val config = PagingConfig(pageSize = K.wordListPageSize, enablePlaceholders = true,
            jumpThreshold = K.wordListJumpThreshold)

        return wordListRepo.getWords(categoryEnum,subCategoryEnum,c,config)
    }
}
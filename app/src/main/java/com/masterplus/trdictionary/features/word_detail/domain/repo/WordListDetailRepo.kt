package com.masterplus.trdictionary.features.word_detail.domain.repo

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.features.word_detail.domain.model.WordListInfoSimilarWords
import kotlinx.coroutines.flow.Flow

interface WordListDetailRepo {

    fun getWords(categoryEnum: CategoryEnum, subCategoryEnum: SubCategoryEnum,
                 c: String?, pageConfig: PagingConfig
    ): Flow<PagingData<WordListInfoSimilarWords>>

    fun getWordsByListId(listId: Int, pageConfig: PagingConfig): Flow<PagingData<WordListInfoSimilarWords>>
}
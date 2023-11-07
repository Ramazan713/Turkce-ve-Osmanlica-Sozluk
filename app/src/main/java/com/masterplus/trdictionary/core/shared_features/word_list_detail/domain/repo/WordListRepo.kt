package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import kotlinx.coroutines.flow.Flow

interface WordListRepo {

    fun getWords(categoryEnum: CategoryEnum, subCategoryEnum: SubCategoryEnum,
                 c: String?,pageConfig: PagingConfig):
            Flow<PagingData<SimpleWordResult>>

    fun getWordsByListId(listId: Int,pageConfig: PagingConfig): Flow<PagingData<SimpleWordResult>>

    suspend fun getAllDictPosByRandomOrder(randomOrder: Int): Int
}
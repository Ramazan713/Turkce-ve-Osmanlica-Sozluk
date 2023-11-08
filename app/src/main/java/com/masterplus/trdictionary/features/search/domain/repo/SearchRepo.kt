package com.masterplus.trdictionary.features.search.domain.repo

import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.features.search.domain.constants.SearchKind
import kotlinx.coroutines.flow.Flow

interface SearchRepo {

    fun search(
        query: String,
        categoryEnum: CategoryEnum,
        searchKind: SearchKind,
        searchCount: Int? = null
    ): Flow<List<WordWithSimilar>>
}
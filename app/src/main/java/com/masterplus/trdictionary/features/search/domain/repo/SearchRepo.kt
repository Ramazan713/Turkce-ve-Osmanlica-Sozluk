package com.masterplus.trdictionary.features.search.domain.repo

import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.features.search.domain.constants.SearchKind

interface SearchRepo {

    suspend fun searchSimple(
        query: String,
        categoryEnum: CategoryEnum,
        searchKind: SearchKind,
        searchCount: Int? = null
    ): List<SimpleWordResult>
}
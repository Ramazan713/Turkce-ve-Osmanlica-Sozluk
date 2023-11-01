package com.masterplus.trdictionary.features.category.presentation.category

import com.masterplus.trdictionary.features.category.domain.models.Category

sealed interface CategoryEvent{

    data class OpenDetail(val category: Category): CategoryEvent

    data object CloseDetail: CategoryEvent
}
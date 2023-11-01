package com.masterplus.trdictionary.features.category.presentation.category

import com.masterplus.trdictionary.features.category.domain.models.Category

data class CategoryState(
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val isDetailOpen: Boolean = false
)

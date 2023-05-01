package com.masterplus.trdictionary.features.savepoint.presentation

import com.masterplus.trdictionary.core.domain.enums.CategoryEnum

sealed class SelectSavePointUiEvent{
    data class NavigateToCategoryRandom(val catEnum: CategoryEnum, val pos: Int): SelectSavePointUiEvent()

    data class NavigateToCategoryAll(val catEnum: CategoryEnum, val pos: Int): SelectSavePointUiEvent()

    data class NavigateToCategoryAlphabetic(val catEnum: CategoryEnum,val c: String,
                                            val pos: Int): SelectSavePointUiEvent()

    data class NavigateToList(val listId: Int, val pos: Int): SelectSavePointUiEvent()
}

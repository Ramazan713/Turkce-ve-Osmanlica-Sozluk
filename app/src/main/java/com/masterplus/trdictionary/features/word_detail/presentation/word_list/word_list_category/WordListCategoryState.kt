package com.masterplus.trdictionary.features.word_detail.presentation.word_list.word_list_category

import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum

data class WordListCategoryState(
    val catEnum: CategoryEnum = CategoryEnum.AllDict,
    val subCategoryEnum: SubCategoryEnum = SubCategoryEnum.All,
    val savePointDestination: SavePointDestination = SavePointDestination.CategoryAll(SavePointType.AllDict),
    val savePointTitle: String = ""
)

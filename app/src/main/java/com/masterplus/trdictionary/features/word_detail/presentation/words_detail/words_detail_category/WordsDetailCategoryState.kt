package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_category

import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.features.word_detail.domain.model.AudioState

data class WordsDetailCategoryState(
    val catEnum: CategoryEnum = CategoryEnum.AllDict,
    val subCategoryEnum: SubCategoryEnum = SubCategoryEnum.All,
    val savePointDestination: SavePointDestination = SavePointDestination.CategoryAll(SavePointType.AllDict),
    val savePointTitle: String = "",
    val audioState: AudioState = AudioState(),
)

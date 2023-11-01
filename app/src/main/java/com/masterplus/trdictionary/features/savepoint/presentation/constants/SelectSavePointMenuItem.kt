package com.masterplus.trdictionary.features.savepoint.presentation.constants

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.*
import com.masterplus.trdictionary.core.util.UiText

enum class SelectSavePointMenuItem(val destinationId: Int): IMenuItemEnum {
    All(destinationId = 0) {
        override val title: UiText
            get() = UiText.Resource(R.string.all)

        override val iconInfo: IconInfo?
            get() = null
    },
    CatAll(SavePointDestination.CategoryAll.destinationId){
        override val title: UiText
            get() = SubCategoryEnum.All.description
        override val iconInfo: IconInfo?
            get() = null

    },
    CatRandom(SavePointDestination.CategoryRandom.destinationId){
        override val title: UiText
            get() = SubCategoryEnum.Random.description
        override val iconInfo: IconInfo?
            get() = null

    },
    CatAlphabetic(SavePointDestination.CategoryAlphabetic.destinationId){
        override val title: UiText
            get() = SubCategoryEnum.Alphabetic.description
        override val iconInfo: IconInfo?
            get() = null
    },
    List(SavePointDestination.List.destinationId) {
        override val title: UiText
            get() = UiText.Resource(R.string.list)
        override val iconInfo: IconInfo?
            get() = null
    };

    companion object{
        private fun fromTypeId(destinationId: Int): SelectSavePointMenuItem {
            return when(destinationId){
                CatAll.destinationId-> CatAll
                CatRandom.destinationId -> CatRandom
                CatAlphabetic.destinationId -> CatAlphabetic
                else -> List
            }
        }

        fun fromDestinationIds(destinationIds: kotlin.collections.List<Int>, addAll: Boolean = false): kotlin.collections.List<SelectSavePointMenuItem> {
            val result = mutableListOf<SelectSavePointMenuItem>()
            if(addAll && destinationIds.size > 1) result.add(All)
            result.addAll(destinationIds.map { fromTypeId(it) })
            return result
        }

    }

}
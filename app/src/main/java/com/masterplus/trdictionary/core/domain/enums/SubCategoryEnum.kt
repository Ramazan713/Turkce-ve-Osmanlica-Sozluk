package com.masterplus.trdictionary.core.domain.enums

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.UiText

enum class SubCategoryEnum(val subCatId: Int,val description: UiText) {
    All(1, UiText.Resource(R.string.all_subCat)),
    Random(2, UiText.Resource(R.string.random)),
    Alphabetic(3, UiText.Resource(R.string.alphabetic));

    companion object{
        fun fromSubCatId(subCatId: Int): SubCategoryEnum {
            return when(subCatId){
                1 -> All
                2 -> Random
                3 -> Alphabetic
                else -> All
            }
        }
    }
}
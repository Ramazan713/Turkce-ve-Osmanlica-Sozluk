package com.masterplus.trdictionary.core.utils

import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum

fun ShortInfoEnum.toWordType(): WordType{
    return when(this){
        ShortInfoEnum.Proverb -> WordType.Proverb
        ShortInfoEnum.Idiom -> WordType.Idiom
        ShortInfoEnum.Word -> WordType.Default
    }
}
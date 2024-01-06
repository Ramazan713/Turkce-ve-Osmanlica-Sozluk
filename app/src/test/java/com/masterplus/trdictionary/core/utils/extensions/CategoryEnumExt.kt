package com.masterplus.trdictionary.core.utils.extensions

import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.enums.WordType

fun CategoryEnum.toDictType(): DictType{
    return when(this){
        CategoryEnum.OsmDict -> DictType.OSM
        CategoryEnum.TrDict -> DictType.TR
        else -> DictType.TR
    }
}

fun CategoryEnum.toRevDictType(): DictType{
    return when(this){
        CategoryEnum.OsmDict -> DictType.TR
        CategoryEnum.TrDict -> DictType.OSM
        else -> DictType.TR
    }
}

fun CategoryEnum.toWordType(): WordType{
    return when(this){
        CategoryEnum.ProverbDict -> WordType.Proverb
        CategoryEnum.IdiomDict -> WordType.Idiom
        else -> WordType.Default
    }
}
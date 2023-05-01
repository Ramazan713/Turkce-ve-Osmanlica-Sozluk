package com.masterplus.trdictionary.core.domain.preferences.model

data class EnumPrefKey<E:Enum<E>>(
    val key: String,
    val default: IEnumPrefValue,
    val from: (Int)-> E
)
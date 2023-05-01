package com.masterplus.trdictionary.core.domain.preferences

import com.masterplus.trdictionary.core.domain.preferences.model.EnumPrefKey
import com.masterplus.trdictionary.core.domain.preferences.model.IEnumPrefValue
import com.masterplus.trdictionary.core.domain.preferences.model.PrefKey

interface AppPreferences {

    fun <T>setItem(item: PrefKey<T>, value: T)

    fun<T>getItem(item: PrefKey<T>): T

    fun <E:Enum<E>> setEnumItem(criteria: EnumPrefKey<E>, value: IEnumPrefValue)

    fun <E:Enum<E>> getEnumItem(criteria: EnumPrefKey<E>): E

    fun clear()

    fun toDict(): Map<String,Any>

    fun fromDict(map: Map<String,Any>)

}
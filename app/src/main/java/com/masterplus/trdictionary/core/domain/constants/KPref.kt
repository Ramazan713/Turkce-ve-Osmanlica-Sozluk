package com.masterplus.trdictionary.core.domain.constants

import com.masterplus.trdictionary.core.domain.enums.SearchResultEnum
import com.masterplus.trdictionary.core.domain.enums.ThemeEnum
import com.masterplus.trdictionary.core.domain.preferences.model.EnumPrefKey
import com.masterplus.trdictionary.core.domain.preferences.model.PrefKey

object KPref {

    val themeEnum = EnumPrefKey("themeEnum",
        ThemeEnum.defaultValue, ThemeEnum::fromKeyValue)

    val searchResultCountEnum = EnumPrefKey("searchResultCountEnum",
        SearchResultEnum.defaultValue, SearchResultEnum::fromKeyValue)


    val themeDynamic = PrefKey("themeDynamic",false)
    val useArchiveLikeList = PrefKey("useArchiveLikeList",false)
    val backupMetaCounter = PrefKey("backupMetaCounter",0L)
    val showBackupSectionForLogin = PrefKey("showBackupSectionForLogin",true)
    val inAppReviewDay = PrefKey("inAppReviewDay",0L)

    val prefValues = listOf<PrefKey<Any>>(themeDynamic, useArchiveLikeList, showBackupSectionForLogin)
    val prefEnumValues = listOf(themeEnum,searchResultCountEnum)
}
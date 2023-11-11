package com.masterplus.trdictionary.core.domain.constants

import com.masterplus.trdictionary.core.domain.enums.ThemeEnum
import com.masterplus.trdictionary.core.domain.preferences.model.EnumPrefKey
import com.masterplus.trdictionary.core.domain.preferences.model.PrefKey

object KPref {

    val themeEnum = EnumPrefKey("themeEnum",
        ThemeEnum.defaultValue, ThemeEnum::fromKeyValue)

    val searchResultCount = PrefKey("searchResultCount", 10)
    val themeDynamic = PrefKey("themeDynamic",false)
    val useArchiveLikeList = PrefKey("useArchiveLikeList",false)
    val backupMetaCounter = PrefKey("backupMetaCounter",0L)
    val showBackupSectionForLogin = PrefKey("showBackupSectionForLogin",true)
    val inAppReviewDay = PrefKey("inAppReviewDay",0L)

    val prefValues = listOf<PrefKey<Any>>(themeDynamic, useArchiveLikeList, showBackupSectionForLogin,searchResultCount)
    val prefEnumValues = listOf(themeEnum)
}
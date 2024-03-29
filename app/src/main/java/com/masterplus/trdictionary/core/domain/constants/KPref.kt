package com.masterplus.trdictionary.core.domain.constants

import com.masterplus.trdictionary.core.domain.enums.ThemeEnum
import com.masterplus.trdictionary.core.domain.preferences.model.EnumPrefKey
import com.masterplus.trdictionary.core.domain.preferences.model.PrefKey

object KPref {

    val backupMetaCounter = PrefKey("backupMetaCounter",0L)
    val inAppReviewDay = PrefKey("inAppReviewDay",0L)
    val thresholdOpeningCount = PrefKey("thresholdOpeningCount",15)
    val consumeIntervalSeconds = PrefKey("consumeIntervalSeconds",4)
    val thresholdConsumeSeconds = PrefKey("thresholdConsumeSeconds",80)
}
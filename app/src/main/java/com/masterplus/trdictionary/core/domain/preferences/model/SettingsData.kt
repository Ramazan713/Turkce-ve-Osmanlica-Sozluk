package com.masterplus.trdictionary.core.domain.preferences.model

import com.masterplus.trdictionary.core.domain.enums.ThemeEnum
import com.masterplus.trdictionary.core.domain.BaseAppSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import javax.inject.Singleton

@Serializable
data class SettingsData(
    val themeEnum: ThemeEnum = ThemeEnum.defaultValue,
    val useThemeDynamic: Boolean = false,
    val useArchiveLikeList: Boolean = false,
    val showBackupSectionForLogin: Boolean = false,
    val searchResultCount: Int = 10
)


@Singleton
class SettingsDataSerializer: BaseAppSerializer<SettingsData>() {
    override val serializer: KSerializer<SettingsData>
        get() = SettingsData.serializer()
    override val defaultValue: SettingsData
        get() = SettingsData()
}
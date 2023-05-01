package com.masterplus.trdictionary.features.settings.presentation

import com.masterplus.trdictionary.core.domain.enums.SearchResultEnum
import com.masterplus.trdictionary.core.domain.model.ThemeModel
import com.masterplus.trdictionary.core.domain.util.UiText
import com.masterplus.trdictionary.features.settings.domain.model.User


data class SettingState(
    val themeModel: ThemeModel = ThemeModel(),
    val searchResultEnum: SearchResultEnum = SearchResultEnum.defaultValue,
    val user: User? = null,
    val isLoading: Boolean = false,
    val useArchiveAsList: Boolean = false,
    val showDialog: Boolean = false,
    val dialogEvent: SettingDialogEvent? = null,
    val showModal: Boolean = false,
    val modalEvent: SettingModalEvent? = null,
    val message: UiText? = null,
    val settingUiEvent: SettingUiEvent? = null
)

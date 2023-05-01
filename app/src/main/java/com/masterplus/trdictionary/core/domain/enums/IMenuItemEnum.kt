package com.masterplus.trdictionary.core.domain.enums

import androidx.compose.ui.graphics.Color
import com.masterplus.trdictionary.core.domain.util.UiText

interface IMenuItemEnum {
    val title: UiText
    val iconInfo: IconInfo?
}

data class IconInfo(
    val drawableId: Int,
    val tintColor: Color? = null,
    val description: UiText?=null
)

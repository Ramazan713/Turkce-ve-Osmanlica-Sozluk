package com.masterplus.trdictionary.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

sealed interface UiColor{
    data class Default(val color: Color): UiColor

    data class ComposeColor(val color: @Composable () -> Color): UiColor

    @Composable
    fun asColor(): Color{
        return when(this){
            is ComposeColor -> color()
            is Default -> color
        }
    }
}

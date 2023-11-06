package com.masterplus.trdictionary.core.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun DefaultIconToggleButton(
    modifier: Modifier = Modifier,
    value: Boolean,
    onValueChange: ((Boolean) -> Unit)?,
    imageVector: ImageVector,
    selectedImageVector: ImageVector = imageVector,
    contentDescription: String? = null,
    tooltip: String? = null,
    selectedTooltip: String? = tooltip,
    enabled: Boolean = true,
    iconTint: Color = LocalContentColor.current,
    selectedIconTint: Color = iconTint
) {

    val currentIconVector = remember(value) {
        if(value) selectedImageVector else imageVector
    }

    val currentTintColor = remember(value) {
        if(value) selectedIconTint else iconTint
    }

    val currentTooltip = remember(value) {
        if(value) selectedTooltip else tooltip
    }

    DefaultToolTip(
        modifier = modifier,
        tooltip = currentTooltip
    ) {
        IconButton(
            modifier = modifier,
            onClick = { onValueChange?.invoke(!value) },
            enabled = enabled
        ) {
            Icon(
                imageVector = currentIconVector,
                contentDescription = contentDescription,
                tint = currentTintColor
            )
        }
    }
}
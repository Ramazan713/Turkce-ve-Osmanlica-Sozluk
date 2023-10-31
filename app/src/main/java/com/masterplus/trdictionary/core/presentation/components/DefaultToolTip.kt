package com.masterplus.trdictionary.core.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultToolTip(
    tooltip: String?,
    enabled: Boolean = true,
    spacingBetweenTooltipAndAnchor: Dp = 4.dp,
    content: @Composable() () -> Unit
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(
            spacingBetweenTooltipAndAnchor = spacingBetweenTooltipAndAnchor
        ),
        tooltip = {
            if(enabled){
                tooltip?.let { text->
                    PlainTooltip {
                        Text(text = text)
                    }
                }
            }
        },
        state = rememberTooltipState()
    ){
        content()
    }
}
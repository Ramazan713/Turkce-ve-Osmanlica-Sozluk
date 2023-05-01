package com.masterplus.trdictionary.core.presentation.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun PrimaryLightButton(
    title: String,
    onClick: ()->Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    useBorder: Boolean = false,
    borderStroke: BorderStroke = BorderStroke(2.dp,MaterialTheme.colorScheme.outline)
){

    ButtonBase(
        onClick = onClick,
        title = title,
        isEnabled = isEnabled,
        modifier = modifier,
        useBorder = useBorder,
        borderStroke = borderStroke,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}
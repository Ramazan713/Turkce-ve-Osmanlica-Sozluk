package com.masterplus.trdictionary.core.presentation.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ButtonBase(
    title: String,
    onClick: ()->Unit,
    colors: ButtonColors,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    useBorder: Boolean = false,
    borderStroke: BorderStroke? = BorderStroke(2.dp,MaterialTheme.colorScheme.outline)
){
    Button(
        onClick = onClick,
        colors = colors,
        shape = MaterialTheme.shapes.medium,
        enabled = isEnabled,
        modifier = modifier.padding(all = 1.dp),
        border = if(useBorder)borderStroke else null
    ){
        Text(title,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
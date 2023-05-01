package com.masterplus.trdictionary.features.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun SelectableText(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    onClick: ()->Unit
){
    val backgroundColor = if(isSelected) MaterialTheme.colorScheme.secondaryContainer else
        MaterialTheme.colorScheme.outlineVariant

    val shape = MaterialTheme.shapes.medium

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .clickable { onClick() }
            .border(2.dp, MaterialTheme.colorScheme.outline,shape)
            .padding(vertical = 13.dp, horizontal = 13.dp)

    ){
        Text(
            title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

package com.masterplus.trdictionary.features.settings.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SettingSectionItem(
    title: String,
    modifier: Modifier = Modifier,
    content:  @Composable (ColumnScope.() -> Unit)
){

    Column(
        modifier = modifier
            .padding(vertical = 7.dp, horizontal = 7.dp)
            .fillMaxWidth()
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium
                .copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(vertical = 3.dp, horizontal = 5.dp)
        )
        content()
    }

}
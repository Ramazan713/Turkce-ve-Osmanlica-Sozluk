package com.masterplus.trdictionary.features.category.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun AlphabeticItem(
    c: String,
    onClicked: ()->Unit,
){
    val shape = MaterialTheme.shapes.medium

    ListItem(
        headlineText = {
            Text(
                c,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                modifier = Modifier
                    .padding(horizontal = 7.dp)

            ) },
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .clip(shape)
            .border(2.dp, MaterialTheme.colorScheme.outline,shape)
            .clickable { onClicked() },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun AlphabeticItem(
    c: String,
    onClicked: ()->Unit,
){
    val shape = MaterialTheme.shapes.medium

    ListItem(
        modifier = Modifier
            .clip(shape)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .clickable { onClicked() },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        headlineContent = {
            Text(
                c,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                modifier = Modifier.padding(horizontal = 7.dp).fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun AlphabeticItemPreview() {
    AlphabeticItem("A"){}
}
package com.masterplus.trdictionary.features.search.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun CustomTextButton(
    text: String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(vertical = 13.dp, horizontal = 17.dp),
    onClicked: (String)->Unit,
){
    val shape = MaterialTheme.shapes.small

    Text(
        text,
        modifier = modifier
            .padding(1.dp)
            .clip(shape)
            .clickable { onClicked(text) }
            .padding(paddingValues)
        ,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
}
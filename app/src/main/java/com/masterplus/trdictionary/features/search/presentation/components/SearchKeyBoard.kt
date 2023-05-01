package com.masterplus.trdictionary.features.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun SearchKeyBoard(
    modifier: Modifier = Modifier,
    tonalElevation: Dp = 9.dp,
    shapes: CornerBasedShape = MaterialTheme.shapes.small,
    onClicked: (String)->Unit,
){
    Surface(
        modifier = modifier,
        tonalElevation = tonalElevation,
        shape = shapes
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp,Alignment.CenterHorizontally)
        ) {
            CustomTextButton("ç", onClicked = onClicked)
            CustomTextButton("ğ",onClicked = onClicked)
            CustomTextButton("ı",onClicked = onClicked)
            CustomTextButton("ö",onClicked = onClicked)
            CustomTextButton("ş",onClicked = onClicked)
            CustomTextButton("â",onClicked = onClicked)
            CustomTextButton("î",onClicked = onClicked)
            CustomTextButton("û",onClicked = onClicked)
        }
    }
}
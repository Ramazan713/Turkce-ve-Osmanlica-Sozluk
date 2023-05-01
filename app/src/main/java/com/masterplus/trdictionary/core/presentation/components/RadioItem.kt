package com.masterplus.trdictionary.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun RadioItem(
   title: String,
   isSelected:Boolean,
   onClick: ()->Unit,
   modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                onClick()
            },
        )
        Spacer(Modifier.width(16.dp))
        Text(title,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
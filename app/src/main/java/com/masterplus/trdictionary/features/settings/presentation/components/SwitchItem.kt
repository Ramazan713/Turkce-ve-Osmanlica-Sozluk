package com.masterplus.trdictionary.features.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun SwitchItem(
    title: String,
    value: Boolean,
    onValueChange: (Boolean)->Unit
){
    Row(
        modifier = Modifier
            .padding(
                horizontal = 1.dp, vertical = 3.dp
            )
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable {
                onValueChange(!value)
            }
            .padding(horizontal = 7.dp, vertical = 5.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        Switch(checked = value, onCheckedChange = onValueChange)
    }
}
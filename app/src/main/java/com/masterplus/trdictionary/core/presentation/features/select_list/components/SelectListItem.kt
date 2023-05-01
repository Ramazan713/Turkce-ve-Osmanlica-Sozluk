package com.masterplus.trdictionary.core.presentation.features.select_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.features.list.domain.model.SelectableListView


@Composable
fun SelectListItem(
    selectableListView: SelectableListView,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onChecked: (Boolean)->Unit,
){
    val shape = MaterialTheme.shapes.medium
    val backgroundColor = if(isSelected) MaterialTheme.colorScheme.primaryContainer else
        MaterialTheme.colorScheme.secondaryContainer

    Row(
        modifier = modifier.padding(
            vertical = 3.dp, horizontal = 5.dp
        )
            .clip(shape)
            .background(backgroundColor,shape)
            .border(1.dp, MaterialTheme.colorScheme.outline,shape)
            .clickable { onChecked(!selectableListView.isSelected) },
        verticalAlignment = Alignment.CenterVertically

    ){
        Checkbox(
            checked = selectableListView.isSelected,
            onCheckedChange = onChecked,
            modifier = Modifier.padding(horizontal = 7.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
                .padding(vertical = 5.dp)
        ) {
            Text(
                selectableListView.listView.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                selectableListView.listView.itemCounts.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }


}
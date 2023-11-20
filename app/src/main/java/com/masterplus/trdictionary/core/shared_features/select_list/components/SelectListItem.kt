package com.masterplus.trdictionary.core.shared_features.select_list.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.presentation.components.DefaultCheckBoxRow
import com.masterplus.trdictionary.core.presentation.utils.SampleDatas
import com.masterplus.trdictionary.features.list.domain.model.SelectableListView



@Composable
fun SelectListItem(
    selectableListView: SelectableListView,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onChecked: (Boolean) -> Unit,
){
    DefaultCheckBoxRow(
        modifier = modifier,
        value = selectableListView.isSelected,
        selectedRow = isSelected,
        onValueChange = onChecked,
        title = selectableListView.listView.name,
        subTitle = selectableListView.listView.itemCounts.toString(),
        shape = MaterialTheme.shapes.medium,
        borderWidth = 1.dp,
        selectedColor = MaterialTheme.colorScheme.primaryContainer,
        defaultColor = MaterialTheme.colorScheme.secondaryContainer
    )
}

@Preview(showBackground = true)
@Composable
fun SelectListItemPreview() {
    SelectListItem(
        selectableListView = SampleDatas.generateSelectableListView(),
        isSelected = true,
        onChecked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SelectListItemPreview2() {
    SelectListItem(
        selectableListView = SampleDatas.generateSelectableListView(),
        isSelected = false,
        onChecked = {}
    )
}
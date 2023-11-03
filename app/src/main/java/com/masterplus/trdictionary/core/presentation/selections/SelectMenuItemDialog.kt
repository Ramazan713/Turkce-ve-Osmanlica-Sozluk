package com.masterplus.trdictionary.core.presentation.selections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.ThemeEnum
import com.masterplus.trdictionary.core.presentation.components.IconLabelRow
import com.masterplus.trdictionary.R

@Composable
fun <T: IMenuItemEnum> SelectMenuItemDialog(
    items: List<T>,
    onClickItem:(T)->Unit,
    onClosed: ()->Unit,
    title: String? = null
){

    AlertDialog(
        onDismissRequest = onClosed,
        title = {
            Text(
                title ?: return@AlertDialog,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items){item->
                    IconLabelRow(
                        margins = PaddingValues(),
                        title = item.title.asString(),
                        borderWidth = 1.dp,
                        shape = MaterialTheme.shapes.medium,
                        iconInfo = item.iconInfo,
                        containerColor = Color.Transparent,
                        onClick = {
                            onClickItem(item)
                            onClosed()
                        }
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onClosed) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SelectMenuItemDialogPreview() {
    SelectMenuItemDialog(
        items = ThemeEnum.values().toList(),
        title = "select item",
        onClickItem = {},
        onClosed = {}
    )
}
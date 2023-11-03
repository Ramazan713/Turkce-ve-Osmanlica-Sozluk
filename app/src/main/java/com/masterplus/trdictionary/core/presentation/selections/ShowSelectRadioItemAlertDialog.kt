package com.masterplus.trdictionary.core.presentation.selections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.ThemeEnum
import com.masterplus.trdictionary.core.presentation.components.DefaultRadioRow


@Composable
fun <T: IMenuItemEnum> ShowSelectRadioItemAlertDialog(
    items: List<T>,
    onClose: () -> Unit,
    title: String? = null,
    onApprove: ((T) -> Unit)? = null,
    selectedItem: (T)? = null,
    imageVector: ImageVector? = null
){
    val currentItem = rememberSaveable {
        mutableStateOf(selectedItem)
    }


    AlertDialog(
        onDismissRequest = { onClose() },
        title = {
            Text(
                title ?: return@AlertDialog,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items){item->
                    DefaultRadioRow(
                        title = item.title.asString(),
                        value = currentItem.value == item,
                        onValueChange = { currentItem.value = item},
                        defaultColor = Color.Transparent,
                        selectedRow = false,
                        margins = PaddingValues()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    currentItem.value?.let { onApprove?.invoke(it) }
                    onClose()
                }
            ) {
                Text(text = stringResource(R.string.approve))
            }
        },
        icon = {
            Icon(imageVector = imageVector ?: return@AlertDialog, contentDescription = null)
        }
    )


}

@Preview(showBackground = true)
@Composable
fun ShowSelectRadioItemAlertDialogPreview() {
    ShowSelectRadioItemAlertDialog(
        items = ThemeEnum.values().toList(),
        title = "Title",
        onClose = {},
        onApprove = {},
        imageVector = Icons.Default.Palette
    )
}


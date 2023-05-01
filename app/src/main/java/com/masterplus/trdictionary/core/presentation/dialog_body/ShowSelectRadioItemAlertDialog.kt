package com.masterplus.trdictionary.core.presentation.dialog_body

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.presentation.components.RadioItem
import com.masterplus.trdictionary.core.presentation.components.buttons.PrimaryButton


@Composable
fun <T: IMenuItemEnum> ShowSelectRadioItemAlertDialog(
    items: List<T>,
    onClose: (T?)->Unit,
    title: String? = null,
    onApprove: ((T)->Unit)? = null,
    selectedItem: (T)? = null,
){
    val currentItem = rememberSaveable {
        mutableStateOf(selectedItem)
    }

    AlertDialog(
        onDismissRequest = { onClose(currentItem.value) },
        confirmButton = {
            PrimaryButton(
                title = stringResource(R.string.approve),
                onClick = {
                    currentItem.value?.let { onApprove?.invoke(it) }
                    onClose(currentItem.value)
                }
            )
        },
        text = {
            LazyColumn {
                if(title!=null){
                    item{
                        Text(
                            title,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 7.dp)
                        )
                    }
                }
                items(items){item->
                    RadioItem(
                        title = item.title.asString(),
                        isSelected = currentItem.value == item,
                        onClick = { currentItem.value = item}
                    )
                }
            }
        }
    )


}
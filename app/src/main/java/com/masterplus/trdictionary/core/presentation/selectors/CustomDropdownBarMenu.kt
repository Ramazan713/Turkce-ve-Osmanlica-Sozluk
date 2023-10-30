package com.masterplus.trdictionary.core.presentation.selectors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.R


@Composable
fun <T: IMenuItemEnum> CustomDropdownBarMenu(
    modifier: Modifier = Modifier,
    items: List<T>,
    onItemChange: ((T)->Unit)? = null,
){
    val context = LocalContext.current
    val shape = MaterialTheme.shapes.small

    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = {expanded = true}, modifier = modifier){
            Icon(painterResource(R.drawable.ic_baseline_more_vert_24),
                contentDescription = stringResource(R.string.menu))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false},
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 3.dp)
        ){
            items.forEach { item->
                val title = item.title.asString(context)
                DropdownMenuItem(
                    text = { Text(title) },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    onClick = {
                        expanded = false
                        onItemChange?.invoke(item)
                    },
                    modifier = Modifier.clip(shape),
                    leadingIcon = {item.iconInfo?.let { iconInfo->
                        Icon(painterResource(iconInfo.drawableId),
                            contentDescription = stringResource(R.string.n_menu_item,title),
                            tint = iconInfo.tintColor?: LocalContentColor.current
                        )
                    }},
                )
            }
        }
    }
}

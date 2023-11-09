package com.masterplus.trdictionary.core.presentation.selections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.DefaultToolTip


@Composable
fun <T: IMenuItemEnum> CustomDropdownBarMenu(
    modifier: Modifier = Modifier,
    items: List<T>,
    onItemChange: ((T)->Unit)? = null,
    icon: ImageVector = Icons.Default.MoreVert,
    contentDescription: String? = stringResource(id = R.string.menu),
    tooltip: String? = contentDescription
){
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    CustomDropdownBarMenu(
        modifier = modifier,
        items = items,
        onItemChange = onItemChange,
        icon = icon,
        contentDescription = contentDescription,
        tooltip = tooltip,
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        }
    )
}


@Composable
fun <T: IMenuItemEnum> CustomDropdownBarMenu(
    modifier: Modifier = Modifier,
    items: List<T>,
    onItemChange: ((T)->Unit)? = null,
    expanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit,
    icon: ImageVector = Icons.Default.MoreVert,
    contentDescription: String? = stringResource(id = R.string.menu),
    tooltip: String? = contentDescription
){
    val context = LocalContext.current
    val shape = MaterialTheme.shapes.small

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DefaultToolTip(tooltip = tooltip) {
            IconButton(onClick = { onExpandedChange(true) }, modifier = modifier){
                Icon(icon, contentDescription = contentDescription)
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
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
                        onExpandedChange(false)
                        onItemChange?.invoke(item)
                    },
                    modifier = Modifier.clip(shape),
                    leadingIcon = {item.iconInfo?.let { iconInfo->
                        Icon(
                            imageVector = iconInfo.imageVector,
                            contentDescription = stringResource(R.string.n_menu_item,title),
                            tint = iconInfo.tintColor?.asColor() ?: LocalContentColor.current
                        )
                    }},
                )
            }
        }
    }
}

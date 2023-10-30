package com.masterplus.trdictionary.core.presentation.selectors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.extensions.noRippleClickable


@Composable
fun <T: IMenuItemEnum>CustomDropdownMenu(
    modifier: Modifier = Modifier,
    items: List<T>,
    onItemChange: ((T)->Unit)? = null,
    currentItem: T? = null,
    showIcons: Boolean = false,
    useBorder: Boolean = true,
    useDefaultBackgroundColor: Boolean = true,
    enabled: Boolean = true
){

    val context = LocalContext.current
    val shape = MaterialTheme.shapes.small

    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant

    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    var currentText by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(currentItem){
        currentText = currentItem?.title?.asString(context) ?:
                items.firstOrNull()?.title?.asString(context) ?: ""
    }


    Column(
        modifier = modifier.padding(5.dp)
            .then(if(useDefaultBackgroundColor)Modifier.background(backgroundColor) else Modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){
        Row(
            modifier = modifier
                .clip(shape = shape)
                .then(if(useBorder)Modifier.border(2.dp, MaterialTheme.colorScheme.outline,shape) else Modifier)
                .background(Color.Transparent)
                .noRippleClickable(enabled = enabled) {
                    expanded = true
                }
                .padding(horizontal = 11.dp, vertical = 9.dp)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                currentText,
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(painter = painterResource(R.drawable.ic_baseline_arrow_drop_down_24),
                contentDescription = stringResource(R.string.dropdown_menu_text), )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false},
            modifier = modifier
                .then(if(useDefaultBackgroundColor)Modifier.background(backgroundColor) else Modifier)
                .padding(horizontal = 3.dp),
        ){
            items.forEach { item->
                val title = item.title.asString(context)
                DropdownMenuItem(
                    enabled = enabled,
                    text = { Text(title, color = LocalContentColor.current) },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    onClick = {
                        currentText = item.title.asString(context)
                        expanded = false
                        onItemChange?.invoke(item)
                    },
                    modifier = Modifier.clip(shape),
                    leadingIcon = if(!showIcons) null else {
                        {item.iconInfo?.let { iconInfo->
                            Icon(painterResource(iconInfo.drawableId),
                                contentDescription = stringResource(R.string.n_menu_item,title),
                                tint = iconInfo.tintColor ?: LocalContentColor.current,
                            ) }}
                    }
                )
            }
        }
    }
}
package com.masterplus.trdictionary.core.presentation.selections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.ThemeEnum
import com.masterplus.trdictionary.core.extensions.noRippleClickable
import com.masterplus.trdictionary.core.extensions.useBackground
import com.masterplus.trdictionary.core.extensions.useBorder


@Composable
fun <T: IMenuItemEnum> CustomDropdownMenu(
    modifier: Modifier = Modifier,
    items: List<T>,
    onItemChange: ((T)->Unit)? = null,
    currentItem: T? = null,
    showIcons: Boolean = false,
    borderWidth: Dp? = 1.dp,
    backgroundColor: Color? = MaterialTheme.colorScheme.surfaceVariant,
    enabled: Boolean = true
){

    val context = LocalContext.current
    val shape = MaterialTheme.shapes.small

    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    val imageVector = remember(expanded) {
        if(expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
    }

    var currentText by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(currentItem){
        currentText = currentItem?.title?.asString(context) ?:
                items.firstOrNull()?.title?.asString(context) ?: ""
    }


    Column(
        modifier = modifier
            .padding(5.dp)
            .clip(shape = shape)
            .useBorder(borderWidth, shape = shape)
            .useBackground(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){
        Row(
            modifier = modifier
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
            Icon(
                imageVector = imageVector,
                contentDescription = stringResource(R.string.dropdown_menu_text),
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false},
            modifier = modifier
                .useBackground(backgroundColor)
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
                        {
                            item.iconInfo?.let { iconInfo->
                                Icon(
                                    painterResource(iconInfo.drawableId),
                                    contentDescription = stringResource(R.string.n_menu_item,title),
                                    tint = iconInfo.tintColor ?: LocalContentColor.current,
                                    )
                            }
                        }
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CustomDropdownMenuPreview() {
   Column(
       modifier = Modifier
           .height(200.dp)
           .fillMaxWidth()
   ) {
       CustomDropdownMenu(
           items = ThemeEnum.values().toList()
       )
   }
}

@Preview(showBackground = true)
@Composable
fun CustomDropdownMenuPreview2() {
    CustomDropdownMenu(
        items = ThemeEnum.values().toList(),
        backgroundColor = null
    )
}

package com.masterplus.trdictionary.core.presentation.selectors

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.presentation.dialog_body.CustomDialog

@Composable
fun <T: IMenuItemEnum> SelectMenuItemDialog(
    items: List<T>,
    onClickItem:(T)->Unit,
    onClosed: ()->Unit,
    title: String? = null
){

    CustomDialog(
        onClosed = onClosed
    ){
        LazyColumn(
            modifier = Modifier.padding(bottom = 13.dp, top = 3.dp)
        ) {
            item {
                if(title!=null){
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            items(items){item->
                Row(
                    modifier = Modifier
                        .padding(vertical = 3.dp, horizontal = 3.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .clickable {
                            onClickItem(item)
                            onClosed()
                        }
                        .padding(horizontal = 5.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item.iconInfo?.let { iconInfo->
                        Icon(
                            painter = painterResource(iconInfo.drawableId),
                            contentDescription = iconInfo.description?.asString(),
                            modifier = Modifier.size(30.dp).weight(1f),
                            tint = iconInfo.tintColor ?: LocalContentColor.current
                        )
                    }

                    Spacer(Modifier.width(16.dp))
                    Text(
                        item.title.asString(),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W400),
                        modifier = Modifier.weight(5f)
                    )
                }
            }
        }
    }

}
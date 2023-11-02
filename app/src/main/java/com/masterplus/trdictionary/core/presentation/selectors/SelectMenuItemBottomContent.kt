package com.masterplus.trdictionary.core.presentation.selectors

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.presentation.components.DialogHeader
import com.masterplus.trdictionary.core.presentation.components.IconLabelRow
import com.masterplus.trdictionary.features.word_detail.domain.constants.ShareItemEnum


@Composable
fun <T: IMenuItemEnum> SelectMenuItemBottomContent(
    items: List<T>,
    onClickItem: (T) -> Unit,
    title: String? = null,
    onClose: () -> Unit
){
    Column(
        modifier = Modifier
            .padding(bottom = 13.dp, top = 3.dp)
            .padding(horizontal = 2.dp)
    ) {
        if(title != null){
            DialogHeader(
                title = title,
                onIconClick = { onClose() },
                titleStyle = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 9.dp, horizontal = 3.dp),
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items){item->
                IconLabelRow(
                    title = item.title.asString(),
                    iconInfo = item.iconInfo,
                    onClick = { onClickItem(item) },
                    containerColor = Color.Transparent
                )
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun SelectMenuItemBottomContentPreview() {
    SelectMenuItemBottomContent(
        items = ShareItemEnum.values().toList(),
        title = "Title",
        onClickItem = {},
        onClose = {}
    )
}
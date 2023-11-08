package com.masterplus.trdictionary.features.list.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.R


@ExperimentalMaterial3Api
@Composable
fun ListViewItem(
    listView: ListView,
    onClick: ()->Unit,
    trailingItem: @Composable() () -> Unit,
    modifier: Modifier = Modifier
){
    val shape = MaterialTheme.shapes.medium
    val resourceId = if(listView.isRemovable) R.drawable.ic_baseline_library_books_24 else
        R.drawable.ic_baseline_favorite_24

    val tintColor = if(listView.isRemovable) LocalContentColor.current else
        MaterialTheme.colorScheme.error

    Row(
        modifier = modifier.padding(vertical = 3.dp, horizontal = 3.dp)
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.secondaryContainer,shape)
            .border(1.dp,MaterialTheme.colorScheme.outlineVariant,shape)
            .clickable {
                onClick()
            }
            .padding(vertical = 5.dp, horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(painter = painterResource(resourceId),
            tint = tintColor,
            contentDescription = null,modifier = Modifier.padding(start = 7.dp))
        Column(
            modifier = Modifier.weight(1f)
                .padding(horizontal = 17.dp),
        ) {
            Text(
                listView.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                listView.itemCounts.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        trailingItem()
    }
}

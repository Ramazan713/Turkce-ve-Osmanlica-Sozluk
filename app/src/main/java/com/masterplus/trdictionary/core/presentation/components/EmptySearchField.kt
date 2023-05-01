package com.masterplus.trdictionary.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun EmptySearchField(
    title: String,
    onClicked: ()->Unit,
    modifier: Modifier = Modifier,
    onBackPressed: (()->Unit)? = null,
){
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = modifier
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .clip(MaterialTheme.shapes.large)
            .background(backgroundColor,
                MaterialTheme.shapes.large)
            .clickable {
                onClicked()
            }
            .padding(horizontal = if(onBackPressed!=null)1.dp else 13.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ){
        onBackPressed?.let { backPressed->
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable { backPressed() }
                    .padding(vertical = 1.dp, horizontal = 10.dp)
            )
        }?: kotlin.run {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.padding(end = 8.dp)
            )
        }


        Text(
            title,
            style = MaterialTheme.typography.bodyMedium
                .copy(color = contentColor)
        )
    }
}
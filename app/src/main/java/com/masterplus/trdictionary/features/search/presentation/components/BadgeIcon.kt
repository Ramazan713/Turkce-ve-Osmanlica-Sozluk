package com.masterplus.trdictionary.features.search.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.presentation.components.DefaultToolTip
import com.masterplus.trdictionary.features.search.presentation.SearchDialogEvent
import com.masterplus.trdictionary.features.search.presentation.SearchEvent


@ExperimentalMaterial3Api
@Composable
fun BadgeIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    badgeContent: String? = null,
    paddingValues: PaddingValues = PaddingValues(1.dp),
    contentDescription: String? = null,
    tooltip: String? = contentDescription,
    onClicked: () -> Unit
){
    val shape = MaterialTheme.shapes.small


    DefaultToolTip(
        tooltip = tooltip,
        modifier = modifier
    ) {
        Box(
            modifier = modifier
                .padding(1.dp)
                .clip(shape)
                .clickable { onClicked() }
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .height(41.dp)
                    .width(40.dp)
                    .padding(vertical = 3.dp, horizontal = 3.dp)
                ,
                contentAlignment = if(badgeContent != null)
                    Alignment.BottomStart else Alignment.Center
            ) {
                badgeContent?.let { badge->
                    Badge(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ){ Text(badge) }
                }
                Icon(
                    imageVector,
                    contentDescription = contentDescription
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BadgeIconPreview() {
    BadgeIcon(
        Icons.Default.FilterAlt,
        badgeContent = "1",

        onClicked = {
        },
        contentDescription = "Filter"
    )
}
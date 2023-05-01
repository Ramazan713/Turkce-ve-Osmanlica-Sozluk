package com.masterplus.trdictionary.features.search.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@ExperimentalMaterial3Api
@Composable
fun BadgeIcon(
    @DrawableRes resource: Int,
    modifier: Modifier = Modifier,
    badgeContent: String? = null,
    paddingValues: PaddingValues = PaddingValues(1.dp),
    onClicked: () -> Unit
){
    val shape = MaterialTheme.shapes.small

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
            Icon(painter = painterResource(resource),
                contentDescription = null,)
        }
    }
}
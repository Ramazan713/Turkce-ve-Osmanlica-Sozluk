package com.masterplus.trdictionary.features.category.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun SubCategoryItem(
    title: String,
    @DrawableRes resourceId: Int,
    onClicked: ()->Unit
){
    val shape = MaterialTheme.shapes.medium

    Box(
        Modifier
            .padding(vertical = 10.dp)
            .clip(shape)
            .border(2.dp, MaterialTheme.colorScheme.outline,shape)
            .background(MaterialTheme.colorScheme.secondaryContainer,shape)
            .clickable { onClicked() }
            .padding(vertical = 19.dp, horizontal = 19.dp),
    ){
        Icon(
            painter = painterResource(resourceId),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Text(
            title,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp)
                .align(Alignment.Center),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
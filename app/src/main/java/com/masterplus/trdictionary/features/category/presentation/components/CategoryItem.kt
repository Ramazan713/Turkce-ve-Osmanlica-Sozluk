package com.masterplus.trdictionary.features.category.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
fun CategoryItem(
    title: String,
    onClicked: ()->Unit,
    @DrawableRes resourceId: Int,
    modifier: Modifier = Modifier
){
    val shape = MaterialTheme.shapes.medium

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(vertical = 7.dp, horizontal = 5.dp)
            .clip(shape)
            .border(2.dp, MaterialTheme.colorScheme.outline,shape)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onClicked() }
            .padding(vertical = 10.dp, horizontal = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        Image(
            painter = painterResource(resourceId),
            modifier = Modifier.size(70.dp).padding(top = 5.dp, bottom = 9.dp),
            contentDescription = null,
        )

        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.weight(1f))
    }
}
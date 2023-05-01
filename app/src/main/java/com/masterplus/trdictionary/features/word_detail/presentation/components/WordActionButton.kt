package com.masterplus.trdictionary.features.word_detail.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.masterplus.trdictionary.core.domain.enums.IconInfo


@Composable
fun WordActionButton(
    iconInfo: IconInfo,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(13.dp),
    size: Dp = 27.dp,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    onClicked: ()->Unit,
){
    val shape = CircleShape

    Box(
        modifier = modifier
            .padding(1.dp)
            .clip(shape)
            .shadow(1.dp)
            .zIndex(1f)
            .background(backgroundColor, shape)
            .clickable(enabled = enabled) { onClicked() }
            .padding(paddingValues)
    ){
        Icon(
            painter = painterResource(iconInfo.drawableId),
            contentDescription = iconInfo.description?.asString(),
            modifier = Modifier.size(size),
            tint = iconInfo.tintColor ?: MaterialTheme.colorScheme.contentColorFor(backgroundColor)
        )
    }
}
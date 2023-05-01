package com.masterplus.trdictionary.features.settings.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun TextIcon(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes resourceId: Int,
    arrangement:  Arrangement.Horizontal = Arrangement.Center
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = arrangement
    ){
        Icon(painter = painterResource(resourceId),contentDescription = null)
        Spacer(Modifier.width(16.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
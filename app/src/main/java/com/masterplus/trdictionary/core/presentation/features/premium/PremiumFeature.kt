package com.masterplus.trdictionary.core.presentation.features.premium

import androidx.compose.foundation.border
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.masterplus.trdictionary.R


@Composable
fun PremiumFeature(
    title: String,
    modifier: Modifier = Modifier,
){
    val shape = MaterialTheme.shapes.medium
    ListItem(
        modifier = modifier
            .clip(shape)
            .border(Dp.Hairline,MaterialTheme.colorScheme.outline,shape),
        leadingContent = { Icon(painter = painterResource(R.drawable.baseline_check_24),contentDescription = null) },
        headlineText = { Text(title) },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )

}
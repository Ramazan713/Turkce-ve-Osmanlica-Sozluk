package com.masterplus.trdictionary.core.presentation.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R

@Composable
fun NavigationBackItem(
    onClick: ()->Unit
){
    IconButton(
        onClick = onClick,
        content = {
            Icon(imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.navigation_back)
            )
        }
    )
}
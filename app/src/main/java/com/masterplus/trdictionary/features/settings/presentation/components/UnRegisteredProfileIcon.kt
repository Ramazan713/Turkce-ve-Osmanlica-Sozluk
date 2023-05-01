package com.masterplus.trdictionary.features.settings.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R


@Composable
fun UnRegisteredProfileIcon(){
    Icon(
        painter = painterResource(R.drawable.ic_baseline_account_circle_24),
        contentDescription = null,
        modifier = Modifier.size(156.dp)
    )
}

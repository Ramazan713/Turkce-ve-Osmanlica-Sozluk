package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R

@Composable
fun OrDivider(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
        Text(
            text = stringResource(id = R.string.or),
            modifier = Modifier
                .padding(horizontal = 12.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}
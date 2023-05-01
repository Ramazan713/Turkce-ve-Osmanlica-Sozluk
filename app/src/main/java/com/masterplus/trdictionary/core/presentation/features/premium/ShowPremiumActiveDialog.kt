package com.masterplus.trdictionary.core.presentation.features.premium

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.presentation.dialog_body.CustomDialog
import com.masterplus.trdictionary.R

@Composable
fun ShowPremiumActiveDialog(
    onClosed: ()->Unit,
){
    CustomDialog(onClosed = onClosed) {
        LazyColumn(
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 9.dp)
                .padding(bottom = 17.dp)
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        stringResource(R.string.premium_active_using_c),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 30.dp)
                            .align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = onClosed,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            item {
                Text(
                    stringResource(R.string.features),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W600),
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                PremiumFeature(
                    title = stringResource(R.string.ad_free)
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
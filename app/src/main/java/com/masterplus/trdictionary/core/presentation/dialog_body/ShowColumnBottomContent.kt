package com.masterplus.trdictionary.core.presentation.dialog_body

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun ShowColumnBottomContent(
    title: String,
    content:  @Composable (LazyItemScope.() -> Unit)
){
    LazyColumn(
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp),
    ) {
        item {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 13.dp, top = 7.dp),
                textAlign = TextAlign.Center
            )
        }
        item {
            content.invoke(this)
        }
    }
}
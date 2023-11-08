package com.masterplus.trdictionary.features.category.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R


@Composable
fun CategoryItem(
    title: String,
    onClicked: () -> Unit,
    @DrawableRes resourceId: Int,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    margins: PaddingValues = PaddingValues(vertical = 7.dp, horizontal = 5.dp)
){
    val shape = MaterialTheme.shapes.medium

    val color = if(selected)MaterialTheme.colorScheme.primaryContainer else
        MaterialTheme.colorScheme.secondaryContainer
    Card(
        modifier
            .padding(margins)
            .clip(shape)
            .clickable { onClicked() }
        ,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        border = BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant),
    ) {

        Row(
            modifier = modifier
                .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            Image(
                painter = painterResource(resourceId),
                modifier = Modifier.size(75.dp),
                contentDescription = title,
            )

            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    CategoryItem(
        title = "testX",
        onClicked = {},
        resourceId = R.drawable.tr_dict
    )
}
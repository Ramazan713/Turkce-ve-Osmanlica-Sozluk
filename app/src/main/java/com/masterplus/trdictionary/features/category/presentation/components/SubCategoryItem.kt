package com.masterplus.trdictionary.features.category.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
fun SubCategoryItem(
    title: String,
    @DrawableRes resourceId: Int,
    onClicked: ()->Unit,
    modifier: Modifier = Modifier,
    margins: PaddingValues = PaddingValues(vertical = 8.dp),
    paddings: PaddingValues = PaddingValues(vertical = 16.dp, horizontal = 12.dp)
){
    val shape = MaterialTheme.shapes.medium

    Card(
        modifier
            .padding(margins)
            .clip(shape)
            .clickable { onClicked() },
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        border = BorderStroke(1.dp,MaterialTheme.colorScheme.outline)
    ) {

        Row(
            modifier = modifier
                .padding(paddings),
            verticalAlignment = Alignment.CenterVertically
        ){

            Icon(
                painter = painterResource(resourceId),
                contentDescription = title,
                modifier = Modifier.size(40.dp)
            )

            Text(
                title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SubCategoryItemPreview() {
    SubCategoryItem(
        title = "testX",
        onClicked = {},
        resourceId = R.drawable.ic_baseline_all_inclusive_24
    )
}
package com.masterplus.trdictionary.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.domain.enums.IconInfo

@Composable
fun IconLabelRow(
    modifier: Modifier = Modifier,
    title: String,
    iconInfo: IconInfo?,
    containerColor: Color? = null,
    borderWidth: Dp? = null,
    shape: Shape = MaterialTheme.shapes.small,
    margins: PaddingValues = PaddingValues(vertical = 3.dp, horizontal = 3.dp),
    paddings: PaddingValues = PaddingValues(horizontal = 5.dp, vertical = 11.dp),
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(margins)
            .fillMaxWidth()
            .clip(shape)
            .clickable { onClick() },
        border = if(borderWidth == null) null else BorderStroke(borderWidth, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(
            containerColor = containerColor ?: CardDefaults.cardColors().containerColor
        ),
        shape = shape,
    ) {
        Row(
            modifier = Modifier
                .padding(paddings),
            verticalAlignment = Alignment.CenterVertically
        ) {
            iconInfo?.let { iconInfo->
                Icon(
                    painter = painterResource(iconInfo.drawableId),
                    contentDescription = iconInfo.description?.asString(),
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1f),
                    tint = iconInfo.tintColor ?: LocalContentColor.current
                )
            }

            Spacer(Modifier.width(16.dp))
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(5f)
            )
        }
    }
}
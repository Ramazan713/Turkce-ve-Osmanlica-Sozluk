package com.masterplus.trdictionary.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun DefaultRadioRow(
    modifier: Modifier = Modifier,
    value: Boolean,
    selectedRow: Boolean = value,
    onValueChange: (Boolean) -> Unit,
    title: String,
    subTitle: String? = null,
    shape: Shape = MaterialTheme.shapes.medium,
    selectedColor: Color = MaterialTheme.colorScheme.primaryContainer,
    defaultColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    borderWidth: Dp? = null,
    margins: PaddingValues = PaddingValues(vertical = 3.dp, horizontal = 5.dp),
    paddings: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
) {

    val containerColor = if(selectedRow) selectedColor else defaultColor

    Card(
        shape = shape,
        border = if(borderWidth == null) null else BorderStroke(borderWidth,
            MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        modifier = modifier
            .padding(margins)
            .clip(shape)
            .toggleable(
                value,
                role = Role.RadioButton,
                onValueChange = onValueChange
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddings),
            verticalAlignment = Alignment.CenterVertically

        ){
            RadioButton(
                selected = value,
                onClick = null,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 5.dp)
                    .padding(start = 4.dp)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge
                )
                if(subTitle != null){
                    Text(
                        subTitle,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }
        }
    }
}
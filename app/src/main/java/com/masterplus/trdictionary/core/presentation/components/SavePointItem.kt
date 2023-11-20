package com.masterplus.trdictionary.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.utils.SampleDatas

@Composable
fun SavePointItem(
    savePoint: SavePoint,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onTitleEditClick: () -> Unit,
    paddings: PaddingValues = PaddingValues(horizontal = 5.dp, vertical = 9.dp),
    margins: PaddingValues = PaddingValues(vertical = 3.dp, horizontal = 3.dp),
){
    val shape = MaterialTheme.shapes.medium

    val backgroundColor = if(isSelected) MaterialTheme.colorScheme.secondaryContainer else
        CardDefaults.cardColors().containerColor


    Card(
        modifier = Modifier
            .padding(margins)
            .fillMaxWidth()
            .clip(shape)
            .clickable { onClick() },
        shape = shape,
        border = BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier.padding(paddings),
            verticalAlignment = Alignment.CenterVertically
        ){
            GetLeadingIconLabel(
                savePoint = savePoint,
                modifier = Modifier.padding(start = 7.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GetTitle(
                    savePoint = savePoint,
                    onTitleEditClick = onTitleEditClick
                )
                Spacer(Modifier.height(8.dp))
                GetBottomDescription(savePoint)
            }

            DefaultToolTip(tooltip = stringResource(id = R.string.delete)) {
                IconButton(onClick = { onDeleteClick() }){
                    Icon(
                        Icons.Default.Delete,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = stringResource(id = R.string.delete),
                    )
                }
            }
        }
    }
}

@Composable
private fun GetLeadingIconLabel(
    savePoint: SavePoint,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painterResource(R.drawable.ic_baseline_save_24),
            contentDescription = stringResource(R.string.n_savepoint,savePoint.id ?: 0),
            modifier = Modifier.requiredSize(30.dp)
        )
        savePoint.autoType.label?.let { label->
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 1.dp)
            )
        }
    }
}


@Composable
private fun GetTitle(
    savePoint: SavePoint,
    onTitleEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        DefaultToolTip(tooltip = stringResource(R.string.edit_title)) {
            Icon(
                painterResource(R.drawable.ic_baseline_edit_24),
                contentDescription = stringResource(R.string.edit_title),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { onTitleEditClick() }
                    .padding(12.dp)
                    .size(22.dp)
            )
        }
        Spacer(Modifier.width(3.dp))
        Text(
            savePoint.title,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun GetBottomDescription(
    savePoint: SavePoint,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            savePoint.savePointDestination.description.asString(),
            modifier = Modifier.padding(horizontal = 1.dp),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            "pos: ${savePoint.itemPosIndex + 1}",
            modifier = Modifier.padding(horizontal = 1.dp),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            savePoint.getReadableDate(),
            modifier = Modifier.padding(horizontal = 1.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}




@Preview(showBackground = true)
@Composable
fun SavePointItemPreview() {
    SavePointItem(
        savePoint = SampleDatas.generateSavePoint(),
        isSelected = false,
        onClick = {},
        onDeleteClick = {},
        onTitleEditClick = {}
    )
}

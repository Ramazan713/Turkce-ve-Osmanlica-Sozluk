package com.masterplus.trdictionary.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.R

@Composable
fun SavePointItem(
    savePoint: SavePoint,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDeleteClick: ()->Unit,
    onTitleEditClick: ()->Unit
){
    val shape = MaterialTheme.shapes.medium

    val backgroundColor = if(isSelected) MaterialTheme.colorScheme.secondaryContainer else
        MaterialTheme.colorScheme.outlineVariant


    Row(
        modifier = Modifier
            .padding(vertical = 3.dp, horizontal = 3.dp)
            .fillMaxWidth()
            .clip(shape)
            .border(2.dp, MaterialTheme.colorScheme.outline,shape)
            .clickable { onClick() }
            .background(backgroundColor)
            .padding(horizontal = 5.dp, vertical = 9.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier = Modifier.padding(start = 7.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painterResource(R.drawable.ic_baseline_save_24),
                contentDescription = stringResource(R.string.n_savepoint,savePoint.id?:0),
                modifier = Modifier.requiredSize(30.dp)
            )
            savePoint.autoType.label?.let { label->
                Text(label)
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painterResource(R.drawable.ic_baseline_edit_24),
                    contentDescription = stringResource(R.string.edit_title),
                    modifier = Modifier.clip(MaterialTheme.shapes.medium).clickable {
                        onTitleEditClick()
                    }.padding(5.dp)
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    savePoint.title,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
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
        IconButton(onClick = {onDeleteClick()}){
            Icon(
                Icons.Default.Delete,
                tint = MaterialTheme.colorScheme.error,
                contentDescription = null,
            )
        }
    }
}
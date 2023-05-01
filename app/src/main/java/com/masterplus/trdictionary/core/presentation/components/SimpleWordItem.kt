package com.masterplus.trdictionary.core.presentation.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult


@ExperimentalFoundationApi
@Composable
fun SimpleWordItem(
    simpleWord: SimpleWordResult,
    modifier: Modifier = Modifier,
    order: Int? = null,
    meaningMaxLines: Int = Int.MAX_VALUE,
    onLongClicked: (()->Unit)? = null,
    onClicked: () -> Unit,
){
    val shape = MaterialTheme.shapes.medium

    Row(
        modifier = modifier
            .padding(1.dp)
            .clip(shape)
            .border(2.dp, MaterialTheme.colorScheme.outline,shape)
            .background(MaterialTheme.colorScheme.secondaryContainer,shape)
            .combinedClickable(
                onClick = onClicked,
                onLongClick = onLongClicked
            )
            .padding(vertical = 7.dp, horizontal = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(painter = painterResource(simpleWord.dictType.resourceId),
            modifier = Modifier.padding(horizontal = 3.dp).size(30.dp),
            contentDescription = null)
        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 7.dp)
        ){
            Text(
                simpleWord.wordContent,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                modifier = Modifier.padding(bottom = 1.dp)
            )
            Text(
                simpleWord.meaning,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = meaningMaxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(order!=null){
                Text(
                    order.toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Icon(painter = painterResource( R.drawable.ic_baseline_keyboard_arrow_right_24),
                contentDescription = null)
        }

    }

}

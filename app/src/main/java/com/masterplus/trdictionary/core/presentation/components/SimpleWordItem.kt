package com.masterplus.trdictionary.core.presentation.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar


@ExperimentalFoundationApi
@Composable
fun SimpleWordItem(
    word: WordWithSimilar,
    modifier: Modifier = Modifier,
    order: Int? = null,
    selected: Boolean = false,
    meaningMaxLines: Int = Int.MAX_VALUE,
    onLongClicked: (()->Unit)? = null,
    onClicked: () -> Unit,
){
    SimpleWordItem(
        wordContent = word.wordDetail.word,
        dictType = word.wordDetail.dictType,
        meaning = word.wordDetailMeanings.meanings.firstOrNull()?.meaning?.meaning ?: "",
        modifier = modifier,
        order = order,
        selected = selected,
        meaningMaxLines = meaningMaxLines,
        onLongClicked = onLongClicked,
        onClicked = onClicked
    )
}

@ExperimentalFoundationApi
@Composable
fun SimpleWordItem(
    simpleWord: SimpleWordResult,
    modifier: Modifier = Modifier,
    order: Int? = null,
    selected: Boolean = false,
    meaningMaxLines: Int = Int.MAX_VALUE,
    onLongClicked: (()->Unit)? = null,
    onClicked: () -> Unit,
){
    SimpleWordItem(
        wordContent = simpleWord.wordContent,
        dictType = simpleWord.dictType,
        meaning = simpleWord.meaning,
        modifier = modifier,
        order = order,
        selected = selected,
        meaningMaxLines = meaningMaxLines,
        onLongClicked = onLongClicked,
        onClicked = onClicked
    )
}

@ExperimentalFoundationApi
@Composable
fun SimpleWordItem(
    wordContent: String,
    dictType: DictType,
    meaning: String,
    modifier: Modifier = Modifier,
    order: Int? = null,
    selected: Boolean = false,
    meaningMaxLines: Int = Int.MAX_VALUE,
    onLongClicked: (()->Unit)? = null,
    onClicked: () -> Unit,
){
    val shape = MaterialTheme.shapes.medium
    
    val containerColor = if(selected) MaterialTheme.colorScheme.primaryContainer else
        MaterialTheme.colorScheme.secondaryContainer

    Card(
        modifier = modifier
            .padding(1.dp)
            .clip(shape)
            .combinedClickable(
                onClick = onClicked,
                onLongClick = onLongClicked
            ),
        shape = shape,
        border = BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 7.dp, horizontal = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(dictType.resourceId),
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .size(30.dp),
                contentDescription = dictType.name
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 7.dp)
            ){
                Text(
                    wordContent,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                    modifier = Modifier.padding(bottom = 1.dp)
                )
                Text(
                    meaning,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = meaningMaxLines,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(order != null){
                    Text(
                        order.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_keyboard_arrow_right_24),
                    contentDescription = null
                )
            }

        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun SimpleWordItemPreview() {
    SimpleWordItem(
        wordContent = "wordContent",
        dictType = DictType.TR,
        meaning = "meaning",
        order = 1,
        onLongClicked = {  },
        onClicked = {  }
    )
}

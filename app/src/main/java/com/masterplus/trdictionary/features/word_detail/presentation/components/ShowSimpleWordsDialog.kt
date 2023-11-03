package com.masterplus.trdictionary.features.word_detail.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.presentation.components.DialogHeader
import com.masterplus.trdictionary.core.presentation.components.SimpleWordItem
import com.masterplus.trdictionary.core.presentation.dialog_body.CustomDialog
import com.masterplus.trdictionary.core.util.SampleDatas


@ExperimentalFoundationApi
@Composable
fun ShowSimpleWordsDialog(
    title: String,
    words: List<SimpleWordResult>,
    onClosed: ()->Unit,
    onClickedWord: (SimpleWordResult)->Unit,
    windowWidthSizeClass: WindowWidthSizeClass
){
    CustomDialog(
        onClosed = {onClosed()},
        adaptiveWidthSizeClass = windowWidthSizeClass
    ){

        Column(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 3.dp)
                .padding(bottom = 7.dp)
        ) {

            DialogHeader(
                title = title,
                onIconClick = onClosed,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
            )

            LazyColumn {
                itemsIndexed(
                    words,
                    key = {_,w->w.wordId}
                ){index,word->
                    SimpleWordItem(
                        order = index + 1,
                        simpleWord = word,
                        onClicked = {
                            onClickedWord(word)
                        }
                    )
                }

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun ShowSimpleWordsDialogPreview() {

    val words = listOf(
        SampleDatas.generateSimpleWordResult(wordId = 1),
        SampleDatas.generateSimpleWordResult(wordId = 3)
    )

    ShowSimpleWordsDialog(
        title = "Test title",
        words = words,
        onClosed = {},
        onClickedWord = {},
        windowWidthSizeClass = WindowWidthSizeClass.Compact
    )
}


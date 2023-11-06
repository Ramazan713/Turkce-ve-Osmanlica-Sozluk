package com.masterplus.trdictionary.core.presentation.features.word_list_detail.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.presentation.features.share.domain.enums.ShareItemEnum
import com.masterplus.trdictionary.features.word_detail.domain.model.AudioState
import com.masterplus.trdictionary.features.word_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.features.word_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.features.word_detail.presentation.components.WordDetailItem
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailDialogEvent
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailEvent
import com.masterplus.trdictionary.core.presentation.features.word_list_detail.WordsListDetailSheetEvent

@Composable
fun WordsDetailAdaptiveContent(
    wordWithSimilar: WordWithSimilar,
    windowWidthSizeClass: WindowWidthSizeClass,
    audioState: AudioState? = null,
    onProverbIdiomWordsClicked: (WordDetailMeanings) -> Unit,
    onCompoundWordsClicked: (WordDetailMeanings) -> Unit,
    onVolumePressed: (()->Unit)? = null,
    onFavoritePressed: (()->Unit)? = null,
    onSelectListPressed: (()->Unit)? = null,
    onShareMenuItemClicked: (ShareItemEnum)->Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(400.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ){
        itemsIndexed(
            wordWithSimilar.allWords,
            key = {_,it -> it.wordDetail.id}
        ){index,wordDetail->
            WordDetailItem(
                wordMeanings = wordDetail,
                showButtons = index == 0,
                audioState = audioState,
                onShareMenuItemClicked = onShareMenuItemClicked,
                onFavoritePressed = onFavoritePressed,
                onSelectListPressed = onSelectListPressed,
                onVolumePressed = onVolumePressed,
                onProverbIdiomWordsClicked = onProverbIdiomWordsClicked,
                onCompoundWordsClicked = onCompoundWordsClicked,
                windowWidthSizeClass = windowWidthSizeClass
            )
        }
    }
}


@Composable
fun WordsDetailAdaptiveContent(
    wordWithSimilar: WordWithSimilar,
    windowWidthSizeClass: WindowWidthSizeClass,
    audioState: AudioState? = null,
    onEvent: (WordsListDetailEvent) -> Unit,
    onFavoritePressed: (()->Unit)? = null,
){
    WordsDetailAdaptiveContent(
        wordWithSimilar = wordWithSimilar,
        windowWidthSizeClass = windowWidthSizeClass,
        audioState = audioState,
        onShareMenuItemClicked = {
            onEvent(
                WordsListDetailEvent.ShareWord(it,wordWithSimilar.wordDetail)
            )
        },
        onFavoritePressed = onFavoritePressed,
        onSelectListPressed = {
            onEvent(
                WordsListDetailEvent.ShowSheet(
                WordsListDetailSheetEvent.ShowSelectList(
                    wordWithSimilar.wordId,
                ))
            )
        },
        onVolumePressed = {
            onEvent(WordsListDetailEvent.ListenWords(wordWithSimilar.wordDetail))
        },
        onProverbIdiomWordsClicked = {
            onEvent(
                WordsListDetailEvent.ShowDialog(
                    WordsListDetailDialogEvent.ShowProverbIdiomsWordsList(it)
                )
            )
        },
        onCompoundWordsClicked = {
            onEvent(
                WordsListDetailEvent.ShowDialog(
                    WordsListDetailDialogEvent.ShowCompoundWordsList(it)
                )
            )
        }
    )
}

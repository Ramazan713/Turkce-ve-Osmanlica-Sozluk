package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.masterplus.trdictionary.core.shared_features.share.domain.enums.ShareItemEnum
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.AudioState
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.components.WordDetailItem
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailDialogEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailEvent
import com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.WordsListDetailSheetEvent

@Composable
fun WordsDetailAdaptiveContent(
    modifier: Modifier = Modifier,
    wordWithSimilar: WordWithSimilar,
    windowWidthSizeClass: WindowWidthSizeClass,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(12.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp),
    gridCells: GridCells = GridCells.Adaptive(400.dp),
    audioState: AudioState? = null,
    onProverbIdiomWordsClicked: (WordDetailMeanings) -> Unit,
    onCompoundWordsClicked: (WordDetailMeanings) -> Unit,
    onVolumePressed: (()->Unit)? = null,
    onFavoritePressed: (()->Unit)? = null,
    onShareMenuItemClicked: (ShareItemEnum)->Unit,
    onDetailSelectListPressed: () -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = contentPadding,
        columns = gridCells,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement
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
                onSelectListPressed = onDetailSelectListPressed,
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
    modifier: Modifier = Modifier,
    wordWithSimilar: WordWithSimilar,
    windowWidthSizeClass: WindowWidthSizeClass,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(12.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp),
    gridCells: GridCells = GridCells.Adaptive(400.dp),
    audioState: AudioState? = null,
    onEvent: (WordsListDetailEvent) -> Unit,
    onFavoritePressed: (()->Unit)? = null,
    onDetailSelectListPressed: () -> Unit
){
    WordsDetailAdaptiveContent(
        modifier = modifier,
        wordWithSimilar = wordWithSimilar,
        windowWidthSizeClass = windowWidthSizeClass,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        gridCells = gridCells,
        audioState = audioState,
        onShareMenuItemClicked = {
            onEvent(
                WordsListDetailEvent.ShareWord(it,wordWithSimilar.wordDetail)
            )
        },
        onFavoritePressed = onFavoritePressed,
        onDetailSelectListPressed = onDetailSelectListPressed,
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
        },
    )
}


@Composable
fun WordsDetailAdaptiveContent(
    modifier: Modifier = Modifier,
    wordWithSimilar: WordWithSimilar,
    windowWidthSizeClass: WindowWidthSizeClass,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(12.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp),
    gridCells: GridCells = GridCells.Adaptive(400.dp),
    audioState: AudioState? = null,
    onEvent: (WordsListDetailEvent) -> Unit,
){
    WordsDetailAdaptiveContent(
        modifier = modifier,
        wordWithSimilar = wordWithSimilar,
        windowWidthSizeClass = windowWidthSizeClass,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        gridCells = gridCells,
        audioState = audioState,
        onEvent = onEvent,
        onFavoritePressed = {
            onEvent(WordsListDetailEvent.AddFavorite(wordWithSimilar.wordId,null,null))
        },
        onDetailSelectListPressed = {
            onEvent(
                WordsListDetailEvent.ShowSheet(
                WordsListDetailSheetEvent.ShowSelectList(wordWithSimilar.wordId,null)
            ))
        }
    )
}